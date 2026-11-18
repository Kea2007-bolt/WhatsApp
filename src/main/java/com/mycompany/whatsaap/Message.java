/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.whatsaap;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
    private static int totalMessagesCreated = 0;

    public Message(String recipient, String content) {
        this.recipient = recipient;
        this.content = content;
        this.messageId = generateMessageId();
        this.messageNumber = ++totalMessagesCreated;
        this.messageHash = createMessageHash();
    }

    public String createMessageHash() {
        String firstTwo = messageId.substring(0, 2);
        String[] words = content.trim().split("\\s+");
        String firstWord = words[0].toUpperCase().replaceAll("[^A-Z0-9]", "");
        String lastWord = words[words.length - 1].toUpperCase().replaceAll("[^A-Z0-9]", "");
        return firstTwo + messageNumber + firstWord + lastWord;
    }

    @SuppressWarnings("unchecked")
    public void storeMessage() {
        JSONParser parser = new JSONParser();
        JSONArray messagesArray = new JSONArray();
        try (FileReader reader = new FileReader("messages.json")) {
            Object obj = parser.parse(reader);
            messagesArray = (JSONArray) obj;
        } catch (IOException | ParseException e) { }

        JSONObject msgObj = new JSONObject();
        msgObj.put("messageId", messageId);
        msgObj.put("messageNumber", messageNumber);
        msgObj.put("recipient", recipient);
        msgObj.put("content", content);
        msgObj.put("messageHash", messageHash);
        messagesArray.add(msgObj);

        try (FileWriter writer = new FileWriter("messages.json")) {
            writer.write(messagesArray.toJSONString());
        } catch (IOException e) {
            System.err.println("Error saving: " + e.getMessage());
        }
    }

    public String sendMessage(String choice) {
        switch (choice.trim().toLowerCase()) {
            case "send":
                MessageStore.addSentMessage(this);
                return "Message successfully sent.";
            case "discard":
                MessageStore.addDisregardedMessage(this);
                return "Press 0 to delete the message.";
            case "store":
                storeMessage();
                MessageStore.addStoredMessage(this);
                return "Message successfully stored.";
            default:
                return "Invalid choice.";
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
        if (text == null || text.isEmpty()) return "Message cannot be empty.";
        if (text.length() <= 250) return "Message ready to send.";
        int over = text.length() - 250;
        return "Message exceeds 250 characters by " + over + "; please reduce the size.";
    }

    public static boolean checkRecipientCell(String cell) {
        Pattern pattern = Pattern.compile("^\\+27[0-9]{9}$");
        return cell != null && pattern.matcher(cell).matches();
    }

    public static String getRecipientCellFeedback(String cell) {
        if (checkRecipientCell(cell)) return "Cell phone number successfully captured.";
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String getMessageId() { return messageId; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getContent() { return content; }
    public String getMessageHash() { return messageHash; }
    public static int getTotalMessagesCreated() { return totalMessagesCreated; }
    public static void resetTotalCounter() { totalMessagesCreated = 0; }
}

