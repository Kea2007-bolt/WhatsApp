/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.whatsaap;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 *
 * @author Student
 */
public class Message {
   



    private String messageId;
    private int messageNumber;
    private String recipient;
    private String content;
    private String messageHash;

    private static final List<Message> allSentMessages = new ArrayList<>();
    private static int totalMessagesSent = 0;

    public Message(String recipient, String content) {
        this.recipient = recipient;
        this.content = content;
        this.messageId = generateMessageId();
        this.messageNumber = ++totalMessagesSent;
        this.messageHash = createMessageHash();
    }

    public static boolean checkMessageID(String id) {
        return id != null && id.length() <= 10;
    }

    public static String checkRecipientCell(String cell) {
        Pattern pattern = Pattern.compile("^\\+27[0-9]{9}$");
        Matcher matcher = pattern.matcher(cell);
        if (matcher.matches()) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public String createMessageHash() {
        String firstTwo = messageId.substring(0, 2);
        String[] words = content.trim().split("\\s+");
        String firstWord = words[0].toUpperCase();
        String lastWord = words[words.length - 1].toUpperCase();
        return String.format("%s:%d:%s%s", firstTwo, messageNumber, firstWord, lastWord);
    }

    public String sendMessage(String choice) {
        switch (choice.trim().toLowerCase()) {
            case "send":
                allSentMessages.add(this);
                return "Message successfully sent.";
            case "discard":
                return "Press 0 to delete the message.";
            case "store":
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid choice.";
        }
    }

    public static String printMessages() {
        if (allSentMessages.isEmpty()) return "No messages sent yet.";
        StringBuilder sb = new StringBuilder();
        for (Message m : allSentMessages) {
            sb.append("Message ID: ").append(m.messageId).append("\n");
            sb.append("Message Hash: ").append(m.messageHash).append("\n");
            sb.append("Recipient: ").append(m.recipient).append("\n");
            sb.append("Message: ").append(m.content).append("\n");
            sb.append("------------------------\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    @SuppressWarnings("unchecked")
    public void storeMessage() {
        JSONObject msgObj = new JSONObject();
        msgObj.put("messageId", messageId);
        msgObj.put("messageNumber", messageNumber);
        msgObj.put("recipient", recipient);
        msgObj.put("content", content);
        msgObj.put("messageHash", messageHash);

        JSONArray messagesArray = new JSONArray();
        messagesArray.add(msgObj);

        try (FileWriter writer = new FileWriter("messages.json")) {
            writer.write(messagesArray.toJSONString());
        } catch (IOException e) {
            System.err.println("Error saving message: " + e.getMessage());
        }
    }

    private String generateMessageId() {
        Random rand = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(rand.nextInt(10));
        }
        return id.toString();
    }

    public static String checkMessageLength(String text) {
        if (text.length() <= 250) {
            return "Message ready to send.";
        } else {
            int over = text.length() - 250;
            return "Message exceeds 250 characters by " + over + "; please reduce the size.";
        }
    }

    public String getMessageId() { return messageId; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getContent() { return content; }
    public String getMessageHash() { return messageHash; }
}

