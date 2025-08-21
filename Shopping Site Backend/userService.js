// export const handler = async (event) => {
// import {
//    DynamoDBClient
// } from "@aws-sdk/client-dynamodb";
// import {
//    DynamoDBDocumentClient,
//    ScanCommand,
//    PutCommand,
//    GetCommand,
//    UpdateCommand,
//    DeleteCommand,
// } from "@aws-sdk/lib-dynamodb";

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

// dynamo table name
//Make sure your table name matches this
const dynamoTableName = "Accounts-UserInformation";
// dynamo region
const dynamoTableRegion = "us-east-1";

const dynamoDBClient = new DynamoDBClient({
   region: dynamoTableRegion
});
const dynamo = DynamoDBDocumentClient.from(dynamoDBClient);

// define all request methods here
const REQUEST_METHOD = {
   POST: "POST",
   GET: "GET",
   DELETE: "DELETE",
   PATCH: "PATCH",
};

// define all status codes here
const STATUS_CODE = {
   SUCCESS: 200,
   NOT_FOUND: 404,
   BAD_REQUEST: 400,
   SERVER_ERROR: 500,
};

// paths
const accountsPath = "/accounts";
const userPath = "/account";
const userParamPath = `${userPath}/{id}`;
const loginPath = `${userPath}/login`;


export const handler = async (event, context) => {
         console.log("Request event method: ", event.httpMethod);
         console.log("EVENT\n" + JSON.stringify(event, null, 2));

         let response;

         switch (true) {
            // Get user by id
            case event.httpMethod == REQUEST_METHOD.GET &&
            event.requestContext.resourcePath === userParamPath:
               response = await getUser(Number(event.pathParameters.id));
               break;
            case event.httpMethod == REQUEST_METHOD.GET &&
            event.requestContext.resourcePath === accountsPath:
               response = await retrieveAccounts(String(event.queryStringParameters.accountType), String(event.queryStringParameters.active));
               break;
            //Add new account
            case event.httpMethod == REQUEST_METHOD.POST &&
            event.requestContext.resourcePath === accountsPath:
               response = await addAccount(JSON.parse(event.body));
               break;
            //Login a user
            case event.httpMethod == REQUEST_METHOD.POST &&
            event.requestContext.resourcePath === loginPath:
               response = await loginUser(JSON.parse(event.body));
               break;
               // Password update
            case event.httpMethod === REQUEST_METHOD.PATCH &&
            event.requestContext.resourcePath === accountsPath || userPath:
               response = await updateUser(JSON.parse(event.body));
               break;
               // delete a user
            case event.httpMethod === REQUEST_METHOD.DELETE &&
            event.requestContext.resourcePath === userParamPath:
               response = await deleteUser(Number(event.pathParameters.id));
               break;
         }
         return response;
      };

         //Get user by ID function
         async function getUser(MemberId) {
            const params = {
               TableName: dynamoTableName,
               Key: {
                  id: MemberId,
               },
            };

            console.log("Getting ID: ", MemberId);
            const command = new GetCommand(params);
            try {
               const response = await dynamo.send(command);

               //Make sure the requested item exists
               if (!response.Item) {
                  //Push a 404 error if the requested item is not found
                  return buildResponse(STATUS_CODE.NOT_FOUND, {
                     message: "User not found"
                  });
               } else {
                  return buildResponse(STATUS_CODE.SUCCESS, response.Item);
               }
            }
            //Handle server error/other unexpected errors
            catch (error) {
               if (error.name === "InternalServerError") {
                  return buildResponse(STATUS_CODE.SERVER_ERROR, {
                     message: "Internal server error"
                  });
               } else {
                  return buildResponse(STATUS_CODE.SERVER_ERROR, {
                     message: "Unexpected error"
                  });
               }
            }
         }

         //Get all users with filters function
         async function retrieveAccounts(accountType, active){
            // //Check for valid inputs

            const params = {
               TableName: dynamoTableName,
               FilterExpression: "",
               ExpressionAttributeNames: {},
               ExpressionAttributeValues: {},
            }

            //Update params if account type is a parameter
            if (accountType) {
               params.FilterExpression += "#accountType = :accountType";
               params.ExpressionAttributeNames["#accountType"] = "accountType";
               params.ExpressionAttributeValues[":accountType"] = accountType;
            }

            //Update params if active is a parameter
            if (active) {
               //Check if there is already a filter expression
               if (params.FilterExpression) {
                  params.FilterExpression += " AND ";
               }
               params.FilterExpression += "#active = :active";
               params.ExpressionAttributeNames["#active"] = "active";
               params.ExpressionAttributeValues[":active"] = active;
            }

            try{
               const command = new ScanCommand(params);
               const result = await dynamo.send(command);

               if (!result.Items || result.Items.length === 0) {
                  return buildResponse(STATUS_CODE.NOT_FOUND, {
                     message: "No accounts found"
                  });
               }

               const responseBody = {
                  Operation: "RETRIEVE_ACCOUNTS",
                  Message: "SUCCESS",
                  Accounts: result.Items,
               };
               return buildResponse(STATUS_CODE.SUCCESS, responseBody);
            } catch (error) {
               if (error.name === "InternalServerError") {
                  return buildResponse(STATUS_CODE.SERVER_ERROR, {
                     message: "Internal server error"
                  });
               } else {
                  return buildResponse(STATUS_CODE.SERVER_ERROR, {
                     message: "Unexpected error",
                     details: error.message,
                  });
               }  
            }
         }

         //Add new account function
         async function addAccount(requestBody){
            //Error handling for bad client request
            if (!requestBody.id || !requestBody.accountType ||
               !requestBody.email || !requestBody.password ||
               !requestBody.userName
            ){
               return buildResponse(STATUS_CODE.BAD_REQUEST, {
                  message: "Invalid request."
               });
            }
            const commandParams = {
               TableName: dynamoTableName,
               Item: requestBody,
            };
            const command = new PutCommand(commandParams);
            try {
               await dynamo.send(command);
               const responseBody = {
                  Operation: "ADD",
                  Message: "SUCCESS",
                  Item: requestBody,
               };
               return buildResponse(STATUS_CODE.SUCCESS, responseBody);
            } catch (error) {
               if (error.name === "InternalServerError") {
                  return buildResponse(STATUS_CODE.SERVER_ERROR, {
                     message: "Internal server error",
                     details: error.message,
                  });
               } else {
                  return buildResponse(STATUS_CODE.SERVER_ERROR, {
                     message: "Unexpected error",
                     details: error.message,
                  });
               }
            }
         }

         //Login user function
         async function loginUser(requestBody){
            //Error handling for a bad client request
            if (!requestBody.email || !requestBody.password){
               return buildResponse(STATUS_CODE.BAD_REQUEST, {
                  message: "Invalid request. email and password are required to login."
               });
            }

            //Allow the user to log in as a guest
            if (requestBody.email === "Guest" && requestBody.password === "Guest"){
               return buildResponse(STATUS_CODE.SUCCESS, {
                  message: "Successfully logged in as guest."
               });
            }
            
            const queryParams = {
               TableName: dynamoTableName,
               IndexName: "EmailIndex",
               KeyConditionExpression: "email = :email",
               ExpressionAttributeValues: {
                  ":email": requestBody.email
               }
            };
            const queryCommand = new QueryCommand(queryParams);
            try {
               const result = await dynamo.send(queryCommand);

               //Check if the user was found
               if (result.Items.length === 0){
                  return buildResponse(STATUS_CODE.NOT_FOUND, {
                     message: "User not found"
                  });
               }

               const user = result.Items[0];

               //Check if the password is correct
               if (user.password !== requestBody.password){
                  return buildResponse(STATUS_CODE.BAD_REQUEST, {
                     message: "Incorrect password"
                  });
               }

               //Set the user to active if they successfully log in
               const params = {
                  TableName: dynamoTableName,
                  Key: {
                     id: user.id,
                  },
                  UpdateExpression: 'set #field = :value',
                  ExpressionAttributeNames: {
                     "#field": "active",
                  },
                  ExpressionAttributeValues: {
                     ":value": "Active",
                  },
                  ReturnValues: "ALL_NEW",
               };
               const command = new UpdateCommand(params);
               const response = await dynamo.send(command);


               //If the username and password are correct
               const responseBody = {
                  Operation: "LOGIN",
                  Message: "SUCCESS",
                  Item: user.userName,
               };
               return buildResponse(STATUS_CODE.SUCCESS, responseBody);
            } catch (error) {
               if (error.name === "InternalServerError") {
                  return buildResponse(STATUS_CODE.SERVER_ERROR, {
                     message: "Internal server error",
                     details: error.message,
                  });
               } else {
                  return buildResponse(STATUS_CODE.SERVER_ERROR, {
                     message: "Unexpected error",
                     details: error.message,
                  });
               }
            }
         }

         // Delete user function
         async function deleteUser(userID) {
            const params = {
               TableName: dynamoTableName,
               Key: {
                  id: userID,
               },
               ReturnValues: "ALL_OLD",
            };
            const command = new DeleteCommand(params);
            try {
               const response = await dynamo.send(command);

                //Make sure the requested item exists
               if (!response.Attributes) {
                  //Push a 404 error if the requested item is not found
                  return buildResponse(STATUS_CODE.NOT_FOUND, {
                     message: "User not found"
                  });
               } else {
                  const responseBody = {
                  Operation: "DELETE",
                  Message: "SUCCESS",
                  Item: response.Attributes,
               };
               return buildResponse(STATUS_CODE.SUCCESS, responseBody);
               }
            } catch (error) {
               if (error.name === "InternalServerError") {
                  return buildResponse(STATUS_CODE.SERVER_ERROR, {
                     message: "Internal server error",
                     details: error.message,
                  });
               } else {
                  return buildResponse(STATUS_CODE.SERVER_ERROR, {
                     message: "Unexpected error",
                     details: error.message,
                  });
            }
         }
      }

         //Change user attributes function
         async function updateUser(requestBody) {
            const updatePath = requestBody.updateKey;
         
            const params = {
               TableName: dynamoTableName,
               Key: {
                  id: requestBody.id,
               },
               UpdateExpression: 'set #field = :value',
               ExpressionAttributeNames: {
                  "#field": updatePath,
               },
               ExpressionAttributeValues: {
                  ":value": requestBody.updateValue,
               },
               ReturnValues: "ALL_NEW",
            };
         
            const command = new UpdateCommand(params);
            try {
               const response = await dynamo.send(command);
         
               // Handle a user not found error if no attributes were returned
               if (!response.Attributes) {
                  return buildResponse(STATUS_CODE.NOT_FOUND, {
                     message: "User not found"
                  });
               }
         
               const responseBody = {
                  Operation: "UPDATE",
                  Message: "SUCCESS",
                  UpdatedAttributes: response.Attributes,
               };
               return buildResponse(STATUS_CODE.SUCCESS, responseBody);
            } catch (error) {
               return buildResponse(STATUS_CODE.SERVER_ERROR, {
                  message: "Unexpected error",
                  details: error.message,
               });
            }
         }

         //Helper function to build the response
         function buildResponse(statusCode, body){
            return {
               statusCode: statusCode,
               headers: {
                  "Content-Type": "application/json",
               },
               body: JSON.stringify(body),
            };
         }
         
         // module.exports = {
         //    handler
         // }
