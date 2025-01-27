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
        app.get("/accounts/{account_id}", this::getAllMessagesByUserHandler);
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



}