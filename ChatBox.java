/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package chatbox;

/**
 *
 * @author User
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
//imports ChatBox
public class ChatBox {
    private static HashMap<String, User> users = new HashMap<>();

    public static void main(String[] args) {
        //set up the frame for the ChatBox
        JFrame frame = new JFrame("Chatbox Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 2));
        //object declaration for all labels and text fields
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel cellphoneLabel = new JLabel("Cellphone Number:");
        JTextField cellphoneField = new JTextField();
        
        //object declaration for all buttons 
        JButton createAccountButton = new JButton("Create Account");
        JButton loginButton = new JButton("Login");
        JButton chatButton = new JButton("Chat");
        //Action listener for Create Account button
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String cellphone = cellphoneField.getText();
                //Conditions to be met
                if (username.isEmpty() || password.isEmpty() || cellphone.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields are required!");
                    return;
                }

                if (username.length() > 5 || !username.contains("_")) {
                    JOptionPane.showMessageDialog(frame, "Username must be 5 characters or less and contain an underscore (_).");
                    return;
                }

                if (!password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8}$")) {
                    JOptionPane.showMessageDialog(frame, "Password must be exactly 8 characters long, contain at least one capital letter, one number, and one special character.");
                    return;
                }

                if (!cellphone.matches("\\d{10}")) {
                    JOptionPane.showMessageDialog(frame, "Cellphone number must be 10 digits!");
                    return;
                }

                if (users.containsKey(username)) {
                    JOptionPane.showMessageDialog(frame, "Username already exists!");
                } else {
                    users.put(username, new User(username, password, cellphone));
                    JOptionPane.showMessageDialog(frame, "Account created successfully!");
                }
            }
        });
        //Action listner for log in button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Username and password are required!");
                    return;
                }

                User user = users.get(username);
                if (user != null && user.password.equals(password)) {
                    JOptionPane.showMessageDialog(frame, "Login successful! Welcome KingsChat, " + username + "!");
                    // Proceed to chat
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password!");
                }
            }
        });
        //Action listener for chat button
        chatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
        
                if (!users.containsKey(username)) {
                    JOptionPane.showMessageDialog(frame, "Please log in to access chat functionality.");
                    return;
                }
        
                String recipient = JOptionPane.showInputDialog(frame, "Enter recipient username:");
                if (!users.containsKey(recipient)) {
                    JOptionPane.showMessageDialog(frame, "Recipient does not exist!");
                    return;
                }
        
                String messageContent = JOptionPane.showInputDialog(frame, "Enter your message:");
                if (messageContent == null || messageContent.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Message cannot be empty!");
                    return;
                }
        
                // Send messages
                Message message = new Message(username, recipient, messageContent);
                Message.messages.computeIfAbsent(recipient, k -> new java.util.ArrayList<>()).add(message);
                JOptionPane.showMessageDialog(frame, "Message sent!");
        
                // View chat history of the logged-in user
                StringBuilder chatHistory = new StringBuilder("Chat history with " + recipient + ":\n");
        
                // Add messages sent by the logged-in user
                java.util.List<Message> sentMessages = Message.messages.getOrDefault(recipient, new java.util.ArrayList<>());
                for (Message msg : sentMessages) {
                    if (msg.sender.equals(username)) {
                        chatHistory.append("You: ").append(msg.content)
                                   .append(msg.isRead ? " ***" : "")
                                   .append("\n");
                    }
                }
        
                // Add messages received by the logged-in user from the recipient

                java.util.List<Message> receivedMessages = Message.messages.getOrDefault(username, new java.util.ArrayList<>());
                for (Message msg : receivedMessages) {
                    if (msg.sender.equals(recipient)) {
                        chatHistory.append(recipient).append(": ").append(msg.content)
                                   .append(msg.isRead ? " ***" : "")
                                   .append("\n");
                        msg.isRead = true;
                    }
                }
                 // Display chat history in a dialog
                JOptionPane.showMessageDialog(frame, chatHistory.toString());
            }
        });

        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(cellphoneLabel);
        frame.add(cellphoneField);
        frame.add(createAccountButton);
        frame.add(loginButton);
        frame.add(chatButton);
        frame.setVisible(true);
    }
/// User class to store user information
    static class User {
        String username;
        String password;
        String cellphone;

        User(String username, String password, String cellphone) {
            this.username = username;
            this.password = password;
            this.cellphone = cellphone;
        }
    }
    /// Message class to store message information
    static class Message {
        String sender;
        String recipient;
        String content;
        boolean isRead;
    //ChatBox read reciepts
        Message(String sender, String recipient, String content) {
            this.sender = sender;
            this.recipient = recipient;
            this.content = content;
            this.isRead = false; // 
        }
        private static HashMap<String, java.util.List<Message>> messages = new HashMap<>();
    }
}

