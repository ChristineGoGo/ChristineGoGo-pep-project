package DAO;

import Model.Message;
import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MessageDAO {
    
        /**
     * retrieve all messages from the message table
     * @return all messages
     */
    public List<Message> getAllMessages() {
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message;";
            PreparedStatement pst = conn.prepareStatement(sql);

            ResultSet result = pst.executeQuery();

            while (result.next()) {
                Message message = new Message();
                message.setMessage_id(result.getInt("message_id"));
                message.setPosted_by(result.getInt("posted_by"));
                message.setMessage_text(result.getString("message_text"));
                message.setTime_posted_epoch(result.getInt("time_posted_epoch"));

                messages.add(message);
            }

            return messages;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * get message from database by the id of the message
     * @param message_id
     * @return Message from the message table with the given id 
     */
    public Message getMessageById(int message_id) {
        Connection conn = ConnectionUtil.getConnection();
        Message message = new Message();

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";

            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, message_id);
            
            ResultSet result = pst.executeQuery();

            while (result.next()) {
                message.setMessage_id(result.getInt("message_id"));
                message.setMessage_text(result.getString("message_text"));
                message.setPosted_by(result.getInt("posted_by"));
                message.setTime_posted_epoch(result.getLong("time_posted_epoch"));
            }
            return message;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * add a message to the message table
     * @param message object
     * @return newly returned message
     */
    public Message addMessage(Message message) {
        Connection conn = ConnectionUtil.getConnection();
        Message newMessage = new Message();
        AccountDAO accountDAO = new AccountDAO();

        String message_text = message.getMessage_text();
        int posted_by = message.getPosted_by();
        Account userAccount = accountDAO.getAccountById(posted_by);

        // System.out.println("User account is: " + userAccount);
        // System.out.println("User posted_by is: " + posted_by);
        if ((message_text.length() <= 0) || (userAccount == null)) {
            return null;
        }

        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";

            PreparedStatement pst = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pst.setInt(1, message.getPosted_by());
            pst.setString(2, message.getMessage_text());
            pst.setLong(3, message.getTime_posted_epoch());

            pst.executeUpdate();

            ResultSet pKeyResultSet = pst.getGeneratedKeys();

            if (pKeyResultSet.next()) {
                int generated_message_id = (int) pKeyResultSet.getLong(1);
                newMessage.setMessage_id(generated_message_id);
                newMessage.setPosted_by(message.getPosted_by());
                newMessage.setMessage_text(message.getMessage_text());
                newMessage.setTime_posted_epoch(message.getTime_posted_epoch());
                return newMessage;
            }
            

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * update a message in the message table using the message id
     * @param message_id
     * @param message
     */
    public Message updateMessageById(int message_id, String message_text) {
        Connection conn = ConnectionUtil.getConnection();
        Message message = getMessageById(message_id);
        if ((message == null) || (message_text.length() == 0) || (message_text.length() > 255) ) {
            return null;
        }
            
        
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";


            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, message_text);
            pst.setInt(2, message_id);

            pst.executeUpdate();
            return getMessageById(message_id);

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * delete a message from the message table by its id
     * @param message_id
     */
    public Message deleteMessageById(int message_id) {
        Connection conn = ConnectionUtil.getConnection();
        Message message = getMessageById(message_id);

        try {
            String sql = "DELETE FROM message WHERE message_id = ?;";
            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, message_id);

            pst.executeUpdate();
            return message;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * retrieve a message given the user id
     * @param posted_by
     * @return message
     */

    public List<Message> getMessageByUser(int posted_by) {
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?;";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, posted_by);

            ResultSet rst = pst.executeQuery();

            while (rst.next()) {
                Message message = new Message();
                message.setMessage_id(rst.getInt("message_id"));
                message.setPosted_by(rst.getInt("posted_by"));
                message.setMessage_text(rst.getString("message_text"));
                message.setTime_posted_epoch(rst.getLong("time_posted_epoch"));
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    

}
