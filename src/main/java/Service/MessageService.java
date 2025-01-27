package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

public class MessageService {
        /**
     * for the MessageDAO to map to the message table
     */
    MessageDAO messageDAO;
    /**
     * for the AccountDAO to map to the account table
     */
    AccountDAO accountDAO;
    /**
     * no-args constructor that instantiates a plain messageDAO;
     */
    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }
    /**
     * constructor for messageService when a messageDAO is provided
     * @param messageDAO;
     */
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    /**
     * use the messageDAO to add a new message to the database
     * @param message
     * @return added message
     */
    public Message addMessage(Message message) {
        Message newMessage = messageDAO.addMessage(message);
        Integer generatedMessageId = newMessage == null ? 0 : newMessage.getMessage_id();
        
        if (generatedMessageId > 0) {
            return newMessage;
        }
        return null;
    }

    /**
     * use the messageDAO to update a message in the database
     * @param message_id
     * @return message
     */
    public Message updateMessage(int message_id, String message_text) {
        Message messageToUpdate = messageDAO.getMessageById(message_id);
        if (!(messageToUpdate == null)) {
            return messageDAO.updateMessageById(message_id, message_text);
        }
        return null;

    }
    
    /**
     * get all the messages in the message table
     * @return all available messages
     */
    public List<Message> getMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * get messages using the message_id
     * @param message_id
     * @return messages with the message_id
     */
    public Message getMessagesById(int message_id) {
        Message message = messageDAO.getMessageById(message_id);
        if (message.getMessage_id() > 0) {
            return message;
        }
        return null;
    }

    /**
     * get messages from a specific user
     * @param posted_by
     * @return list of messages from the user 
     */
    public List<Message> getMessagesByUser(int posted_by) {
        Account user = accountDAO.getAccountById(posted_by);

        if (!(user == null)) {
            return messageDAO.getMessageByUser(posted_by);
        }
        return null;
    }

    /**
     * delete a message from the message table
     * @param message_id
     */
    public Message deleteMessage(int message_id) {
        Message message = messageDAO.getMessageById(message_id);
        int receivedMessageId = message.getMessage_id();
        
        if (receivedMessageId > 0) {
            return messageDAO.deleteMessageById(message_id);
        }
        return null;
    }

}
