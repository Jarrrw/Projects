import {
    DynamoDBClient
} from "@aws-sdk/client-dynamodb";
import {
    DynamoDBDocumentClient,
    GetCommand,
    ScanCommand,
    PutCommand,
    UpdateCommand,
    DeleteCommand,
    QueryCommand,
} from "@aws-sdk/lib-dynamodb";

//Dynamo table name
//Make sure your table name matches
const dynamoTableName = "OrderInformation";
//Dynamo region
const dynamoTableRegion = "us-east-1";

const dynamoDBClient = new DynamoDBClient({
    region: dynamoTableRegion
});
const dynamo = DynamoDBDocumentClient.from(dynamoDBClient);

//Define all request methods
const REQUEST_METHOD = {
    POST: "POST",
    GET: "GET",
    DELETE: "DELETE",
    PATCH: "PATCH",
};

//Define all status codes
const STATUS_CODE = {
    SUCCESS: 200,
    CREATED: 201,
    NOT_FOUND: 404,
    BAD_REQUEST: 400,
    SERVER_ERROR: 500,
};

//Paths
const orderPath = "/order";
const ordersPath = "/orders";

export const handler = async (event, context) => {
    console.log("Request event method: ", event.httpMethod);
    console.log("EVENT\n" + JSON.stringify(event, null, 2));

    let response;

    switch(true){
        //Place a new order
        case event.httpMethod === REQUEST_METHOD.POST &&
        event.requestContext.resourcePath === orderPath:
            response = await placeOrder(event);
            break;
        //Get all orders
        case event.httpMethod === REQUEST_METHOD.GET &&
        event.requestContext.resourcePath === orderPath:
            response = await getAllOrders(Number(event.queryStringParameters.UserID), String(event.queryStringParameters.Status));
            break;
        //Modify an order
        case event.httpMethod === REQUEST_METHOD.PATCH &&
        event.requestContext.resourcePath === orderPath:
            response = await modifyOrder(JSON.parse(event.body));
            break;
    }
    return response;
};

//Place order function
async function placeOrder(event){
    try {
        const requestBody = JSON.parse(event.body)
        const Order_ID = requestBody.OrderID;
        const newStatus = requestBody.Status;
        const newItem = requestBody.Item;
        const newPrice = requestBody.Price;
        const User_ID = requestBody.UserID
        
        if (!Order_ID || !newStatus || !newItem || !newPrice || !User_ID) {
            return buildResponse(STATUS_CODE.BAD_REQUEST, {
                error: "Missing fields",
        });
    }    

        const newOrder = {
            OrderID: Order_ID,
            Status: newStatus,
            Item: newItem,
            Price: newPrice,
            UserID: User_ID
        };

        const params = {
            TableName: dynamoTableName,
            Item: newOrder
        };

        await dynamo.send(new PutCommand(params));

        return buildResponse(STATUS_CODE.CREATED, {
            message: "Order Placed",
            orderType: newOrder
        });

    } catch (error) {
        console.error("Encountered error", error);
        return buildResponse(STATUS_CODE.SERVER_ERROR, {
            message: "Unexpected Error",
            details: error.message
        });
    }
}

async function getAllOrders(UserID, Status){
    const params = {
        TableName: dynamoTableName,
        FilterExpression: "",
        ExpressionAttributeNames: {},
        ExpressionAttributeValues: {},
    }

    //Update params if user id is a parameter
    if(UserID){
        params.FilterExpression += "#UserID = :UserID";
        params.ExpressionAttributeNames["#UserID"] = "UserID";
        params.ExpressionAttributeValues[":UserID"] = UserID;
    }

    //Update params if status is a parameter
    if(Status){
        //Check if there is already a filter expression
        if(params.FilterExpression){
            params.FilterExpression += " AND ";
        }
        params.FilterExpression += "#Status = :Status";
        params.ExpressionAttributeNames["#Status"] = "Status";
        params.ExpressionAttributeValues[":Status"] = Status;
    }

    try{
        var getResponseData = "";
        const response = await dynamo.send(new ScanCommand(params));

        if(!response.Items || response.Items.length === 0){
            return buildResponse(STATUS_CODE.NOT_FOUND, {
                message: "No orders found"
            });
        }

        //Process the recieved data
        const responseBody = {
        orders: response.Items,
        getData: getResponseData,
    };
    return buildResponse(STATUS_CODE.SUCCESS, responseBody);
    } catch{
        console.error("Encountered error: ", error);
        return buildResponse(STATUS_CODE.SERVER_ERROR, {
            message: "Unexpected error",
            details: error.message,
        });
    }
}

//Modify an order function
async function modifyOrder(requestBody){
    //Make sure the requested OrderID exists
    const queryParams = {
        TableName: dynamoTableName,
        KeyConditionExpression: "OrderID = :OrderID",
        ExpressionAttributeValues: {
            ":OrderID": requestBody.OrderID
        }
    };
    const queryCommand = new QueryCommand(queryParams);
    const result = await dynamo.send(queryCommand);

    if(result.Items.length === 0){
        return buildResponse(STATUS_CODE.NOT_FOUND, {
            message: "Order ID not found"
        });
    }
    
    console.log("Modify order event");
    const updatePath = requestBody.updateKey
    var posttext = "Patch did not work";
    const params = {
        TableName: dynamoTableName,
        Key: {
            OrderID: requestBody.OrderID,
        },
        UpdateExpression: 'set #field = :value',
        ExpressionAttributeNames: {
            "#field": updatePath,
        },
        ExpressionAttributeValues: {
            ":value": requestBody.updateValue,
        },
        ReturnValues: "UPDATED_NEW",
    };
    const command = new UpdateCommand(params);

    try{
        const dbresponse = await dynamo.send(command);

        //External API call to notifications API
        const posturl = "https://b2vdgus8mj.execute-api.us-east-1.amazonaws.com/dev/notifications";
        
        const extresponse = await fetch(posturl, {
            method: "POST",
            body: JSON.stringify({
                id: requestBody.UserID,
                no_type: requestBody.no_type,
            }),
            headers: {
                "Content-type": "application/json; charset=UTF-8",
            },
        });
        if(extresponse.ok){
            const responseData = await extresponse.json();
            console.log("Recieved external call data: ", responseData);
            posttext = responseData.title;
        } else {
            const errorResponse = {
                message: "Error in API: ${posturl}",
            };
        }
        const responseBody = {
            Operation: "UPDATE",
            Message: "SUCCESS",
            NotificationSent: posttext,
            UpdatedAttributes: dbresponse,
        };
        return buildResponse(STATUS_CODE.SUCCESS, responseBody);
    } catch(error) {
        console.error("Encountered error: ", error);
        return buildResponse(STATUS_CODE.SERVER_ERROR, {
            message: "Unexpected error",
            details: error.message,
        });
    }
}


function buildResponse(statusCode, body){
    return {
       statusCode: statusCode,
       headers: {
          "Content-Type": "application/json",
       },
       body: JSON.stringify(body),
    };
 }
