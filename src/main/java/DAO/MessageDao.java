package DAO;

import Model.Message;
import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MessageDao {
    
    
    /** 
     * Retrieve all message posted by all the users
     * @return all messages
     */

    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message";

            PreparedStatement preparedstatement = connection.prepareStatement(sql);
            ResultSet rs = preparedstatement.executeQuery();

            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),rs.getLong("time_posted_epoch"));
                messages.add(message);
            }

        } catch (SQLException e) {
            System.out.println("Messages not obtained!");
        }
        return messages;
    }

    /** 
     * Retrieve all message posted by a particular user
     * @return all messages byt the user
     */

     public List<Message> getAllMessagesByUser(int user_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";

            PreparedStatement preparedstatement = connection.prepareStatement(sql);
            ResultSet rs = preparedstatement.executeQuery();

            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),rs.getLong("time_posted_epoch"));
                messages.add(message);
            }

        } catch (SQLException e) {
            System.out.println("Messages not obtained!");
        }
        return messages;
    }

}
