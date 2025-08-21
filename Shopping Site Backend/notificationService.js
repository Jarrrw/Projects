// Import required AWS clients and commands
import { DynamoDBClient } from "@aws-sdk/client-dynamodb";
import { DynamoDBDocumentClient, GetCommand, PutCommand, ScanCommand } from "@aws-sdk/lib-dynamodb";

// dynamo table names
const dynamoTableName = "Notification_Types";
const notificationTable = "Notifications";

// dyname region
const dynamoTableRegion = "us-east-1";

// Initialize DynamoDB client and document client
const dynamoDBClient = new DynamoDBClient({ region: dynamoTableRegion });
const dynamo = DynamoDBDocumentClient.from(dynamoDBClient);

// path
const notifications = "/notifications";
const notificationPathParam = "/notifications/{notype}";
const notificationTypesPath = "/Notification_Cat";
const notificationTypesUserNoType = "/notifications/id/{id}";

const STATUS_CODE = {
    SUCCESS: 200,
    CREATED: 201,
    BAD_REQUEST: 400,
    NOT_FOUND: 404,
    ERROR: 500,
};

// New Switch Statement
export const handler = async (event) => {
    console.log("Received event:", JSON.stringify(event, null, 2));

    let response;

    switch (true) {
        // POST request for creating a new notification
        case event.httpMethod === "POST" &&
            event.requestContext.resourcePath === notifications:
            response = await handlePostRequest(event);
            break;

        // POST request for creating new notification type
        case event.httpMethod === "POST" &&
            event.requestContext.resourcePath === notificationTypesPath:
            response = await handlePostNotificationType(event);
            break;

        // GET request for retriving notification by type
        case event.httpMethod === "GET" &&
            event.requestContext.resourcePath === notificationPathParam:
            response = await handleGetRequest(event);
            break;

        // Get request for all notifications
        case event.httpMethod === "GET" &&
            event.requestContext.resourcePath === notifications:
            response = await handleGetAllRequest();
            break;

        // Get request for notification by id and type
        case event.httpMethod === "GET" &&
        event.requestContext.resourcePath === notificationTypesUserNoType:
        response = await handleGetUserNotificationsByType(event);
        break;
        
        default:
            response = buildResponse(STATUS_CODE.BAD_REQUEST, {
                error: "Invalid HTTP Method or Path",
            });
            break;
        }
        return response;
    }

// This handles POST requests to create entry in the notifications table
async function handlePostRequest(event) {
    console.log("Made it to the other API call");
    try {
        const requestBody = JSON.parse(event.body);
        console.log("DB Response:", JSON.stringify(requestBody, null, 2))
        console.log("UserID: ", requestBody.id)
        console.log("updateValue: ", requestBody.no_type);
        const { id, no_type } = requestBody;
        if (!id || !no_type) {
            return buildResponse(STATUS_CODE.BAD_REQUEST, {
                error: "Missing fields id and no_type",
        });
    }

    // Scans notification table to get next notification id
    const notificationId = await getNextNotificationId();

    // Creating new notification entry
    const newNotification = {
        notificationId,
        id: id,
        no_type: no_type,
    };

    // Add new entry to the table
    const params = {
        TableName: notificationTable,
        Item: newNotification,
    };
    await dynamo.send(new PutCommand(params));

    return buildResponse(STATUS_CODE.CREATED, {
        message: "Notification Created Successfully",
        notification: newNotification,
    });
    } catch (error) {
        console.error("Encountered error while creating notification:", error);
       return buildResponse(STATUS_CODE.SERVER_ERROR, {
            message: "Unexpected error",
            details: error.message,
         });
    }

    }

    // Posts new notification type
    async function handlePostNotificationType(event) {
        try {
            const requestBody = JSON.parse(event.body);
            const { no_type, message } = requestBody;
            if ( !no_type || !message ) {
                return buildResponse(STATUS_CODE.BAD_REQUEST, {
                    error: "Missing Fields"
                });
            }

            // Creating new notification type
            const newNotificationType = {
                no_type,
                message,
            };

            // Add new entry to notification types table
            const params = {
                TableName: dynamoTableName,
                Item: newNotificationType,
            };

            await dynamo.send(new PutCommand(params));

            return buildResponse(STATUS_CODE.CREATED, {
                message: "Notification Type Creation Successful",
                notificationType: newNotificationType,
            });
        } catch (error) {
            console.error("Encountered error while creating No_Type:", error);
            return buildResponse(STATUS_CODE.SERVER_ERROR, {
                message: "Unexpected error",
                details: error.message,
            });
        }
    }

    async function handleGetAllRequest() {
        const params = {
            TableName: dynamoTableName,
        };
        const response = await dynamo.send(new ScanCommand(params));
        const responseBody = { 
            notype: response.Items,
        };
        return buildResponse(STATUS_CODE.SUCCESS, responseBody);
    };
    
    // This handles GET requests to retrieve notification by no_type
    async function handleGetRequest(event) {
        const { notype } = event.pathParameters || {};
    
        if (!notype) {
            return buildResponse(STATUS_CODE.BAD_REQUEST, {
                error: "Missing path parameter: notype",
            });
        }
    
        try {
            // Get item with the specified no_type
            const params = {
                TableName: dynamoTableName,
                Key: {
                    no_type: notype,
                },
            };
            const command = new GetCommand(params);
            const result = await dynamo.send(command);
            const item = result.Item;
    
            if (!item) {
                return buildResponse(STATUS_CODE.NOT_FOUND, {
                    error: `Notification with no_type: ${notype} not found`,
                });
            }
            return buildResponse(STATUS_CODE.SUCCESS, {
                message: "Notification retrieved",
                notification: item,
            });
        } catch (error) {
            console.error("Error retrieving notification:", error);
            return buildResponse(STATUS_CODE.ERROR, { error: "Failed to retrieve notification" });
        }
    }

    // Gets next notification id
    async function getNextNotificationId() {
        const params = {
            TableName: notificationTable,
            ProjectionExpression: "notificationId",
        };

        const result = await dynamo.send(new ScanCommand(params));
        const items = result.Items || [];

        // This is what finds the highest notificationID and increments it
        const maxID = items.reduce((max, item) => Math.max(max, item.notificationId || 0), 0);
        return maxID + 1;
    }

    // Gets a notification from the Notification_Types table
    async function getNotification(notype){
        const params = {
            TableName: dynamoTableName,
            Key:{
                notype: notype,
            },
        };

        const command = new GetCommand(params);
        const result = await dynamo.send(command);
        return result.Item;
    }

    // Get request for notifications by user id and type
    async function handleGetUserNotificationsByType(event) {
        const { id } = event.pathParameters || {};
        const notype = event.queryStringParameters ? event.queryStringParameters.notype : null;
    
        if (!id || !notype) {
            return buildResponse(STATUS_CODE.BAD_REQUEST, {
                error: "Missing path parameters: id and notype",
            });
        }
    
        console.log("Retrieving notifications for id:", id, "and type:", notype);
    
        try {
            const params = {
                TableName: notificationTable,
                FilterExpression: "id = :id AND no_type = :notype",
                ExpressionAttributeValues: {
                    ":id": Number(id),
                    ":notype": notype,
                },
            };
    
            const result = await dynamo.send(new ScanCommand(params));
            console.log("Scan result:", JSON.stringify(result.Items, null, 2)); // Log the retrieved items
    
            const items = result.Items || [];
    
            if (items.length === 0) {
                return buildResponse(STATUS_CODE.NOT_FOUND, {
                    error: `No notifications found for id: ${id} and type: ${notype}`,
                });
            }
            return buildResponse(STATUS_CODE.SUCCESS, {
                message: "Notifications retrieved",
                notifications: items,
            });
        } catch (error) {
            console.error("Error retrieving notifications by user id and type:", error);
            return buildResponse(STATUS_CODE.SERVER_ERROR, { error: "Failed to retrieve notifications by user id and type" });
        }
    }

function buildResponse(statusCode, body){
    return {
        statusCode,
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
    };
}