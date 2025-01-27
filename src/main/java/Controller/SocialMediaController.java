package Controller;


import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    /**
     * used to access the database through the messageService
     */

     MessageService messageService;
     /**
      * used to access the database through the accountService
      */
    AccountService accountService;
 
 
     /** initialize the controller with the messageService and accountService */
     public SocialMediaController() {
         messageService = new MessageService();
         accountService = new AccountService();
     }


    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/messages", this::postMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/messages/", this::getMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("messages/{message_id}", this::deleteMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserHandler);
        app.post("/register", this::addUserHandler);
        app.post("/login", this::loginHandler);
        // app.get("example-endpoint", this::exampleHandler);

        return app;
    }

    // /**
    //  * This is an example handler for an example endpoint.
    //  * @param context The Javalin Context object manages information about both the HTTP request and response.
    //  */
    // private void exampleHandler(Context context) {
    //     context.json("sample text");
    // }


    /**
     * handler to add a new message
     * @param ctx
     * @throws  JsonProcessingException
     */
    private void postMessageHandler(Context ctx) throws  JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        
        if (!(addedMessage == null)) {
            ctx.json(mapper.writeValueAsString(addedMessage));
        } else {
            ctx.status(400);
        }
            
    }

    /**
     * handler to update an existing message using its message_id
     * @param ctx
     * @throws JsonProcessingException
     */
    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        String message_text = message.getMessage_text();
        Message messageToUpdate = messageService.updateMessage(message_id, message_text);

        if ((messageToUpdate == null) || (message_id == 0)) {
            ctx.status(400);        
        } else {
            ctx.json(mapper.writeValueAsString(messageToUpdate));
            ctx.status(200);  
        }
   
    }


    /**
     * handler to display all the messages
     * @param ctx
     */
    private void getMessagesHandler(Context ctx) {
        ctx.json(messageService.getMessages());
    }


    /**
     * handler to get message by message_id
     * @param ctx
     * @throws JSONProcessingException
     */
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
      
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessagesById(message_id);

        if (message == null) {
            ctx.status(200);
        } else {
            // ctx.json(messageService.getMessagesById(message_id));
            ctx.json(message);
            ctx.status(200);

        }
    }

    /**
     * handler to delete a message from the database
     * @param ctx 
     */
    private void deleteMessageHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(message_id);

        if (deletedMessage == null) {
            ctx.status(200);
        } else {
            ctx.json(deletedMessage);
        }
    }

    /**
     * handler to display all the messages by a user
     * @param ctx
     * @throws JSONProcessingException
     */
    private void getAllMessagesByUserHandler(Context ctx) {
        int posted_by = Integer.parseInt(ctx.pathParam("account_id"));
        List <Message> messages = messageService.getMessagesByUser(posted_by);
        // int sizeOfMessages = messages == null ? 0 : messages.size();
        // System.out.println("The messages are: " + messages);
        // System.out.println("The size of messages is: " + sizeOfMessages);


        if (messages == null) {
            ctx.status(200);
        } else {
            ctx.json(messages);
        }

        
        
    }


    /**
     * handler to verify user in db and facilitate login
     * @param ctx
     * @throws JsonProcessingException
     */
    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        // String username = ctx.pathParam("username");
        // String password = ctx.pathParam("password");
        // System.out.println("account generated username is: " + account.getUsername());
        Account userAccount = accountService.getAccount(account.getUsername(), account.getPassword());
        int account_id = userAccount == null ? 0 : userAccount.getAccount_id();

        if (account_id > 0) {
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(userAccount));
        } else {
            ctx.status(401);
        }

    }

    /**
     * handler to add a new user to the database
     * @param  ctx
     * @throws JsonProcessingException
     * 
     */
    private void addUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);

        if (addedAccount == null) {
            ctx.status(400);
        } else {
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
    }


}