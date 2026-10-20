/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.whatsaap;

import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 *
 * @author Student
 */
public class MessageStore {
    
    // ========== ALL ARRAYS — NO ArrayList ==========
    private static final Message[] sentMessages = new Message[100];
    private static final Message[] disregardedMessages = new Message[100];
    private static final Message[] storedMessages = new Message[100];
    private static final String[] messageHashes = new String[100];
    private static final String[] messageIDs = new String[100];

    private static int sentCount = 0;
    private static int disregardedCount = 0;
    private static int storedCount = 0;

    public static void addSentMessage(Message m) {
        if (sentCount < sentMessages.length) {
            sentMessages[sentCount] = m;
            messageHashes[sentCount] = m.getMessageHash();
            messageIDs[sentCount] = m.getMessageId();
            sentCount++;
        }
    }

    public static void addDisregardedMessage(Message m) {
        if (disregardedCount < disregardedMessages.length) {
            disregardedMessages[disregardedCount] = m;
            disregardedCount++;
        }
    }

    public static void addStoredMessage(Message m) {
        if (storedCount < storedMessages.length) {
            storedMessages[storedCount] = m;
            messageHashes[storedCount] = m.getMessageHash();
            messageIDs[storedCount] = m.getMessageId();
            storedCount++;
        }
    }

    public static String displayAllStored() {
        if (storedCount == 0) return "No stored messages.\n";
        StringBuilder sb = new StringBuilder("=== All Stored Messages ===\n");
        for (int i = 0; i < storedCount; i++) {
            sb.append("Recipient: ").append(storedMessages[i].getRecipient())
              .append(" | Message: ").append(storedMessages[i].getContent()).append("\n");
        }
        return sb.toString();
    }

    public static String displayLongestMessage() {
        if (storedCount == 0) return "No stored messages.\n";
        int longestIndex = 0;
        int maxLength = storedMessages[0].getContent().length();
        for (int i = 1; i < storedCount; i++) {
            if (storedMessages[i].getContent().length() > maxLength) {
                maxLength = storedMessages[i].getContent().length();
                longestIndex = i;
            }
        }
        return "Longest message: \"" + storedMessages[longestIndex].getContent() + "\"\n" +
               "Recipient: " + storedMessages[longestIndex].getRecipient() + "\n";
    }

    public static String searchByMessageId(String id) {
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].getMessageId().equals(id)) {
                return "Found:\nRecipient: " + storedMessages[i].getRecipient() + "\n" +
                       "Message: " + storedMessages[i].getContent() + "\n";
            }
        }
        return "Message ID " + id + " not found.\n";
    }

    public static String searchByRecipient(String recipientNum) {
        StringBuilder sb = new StringBuilder("Messages to " + recipientNum + ":\n");
        boolean found = false;
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].getRecipient().equals(recipientNum)) {
                sb.append("- ").append(storedMessages[i].getContent()).append("\n");
                found = true;
            }
        }
        if (!found) return "No messages found for recipient " + recipientNum + ".\n";
        return sb.toString();
    }

    public static String deleteByHash(String hash) {
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].getMessageHash().equals(hash)) {
                String deletedContent = storedMessages[i].getContent();
                for (int j = i; j < storedCount - 1; j++) {
                    storedMessages[j] = storedMessages[j + 1];
                    messageHashes[j] = messageHashes[j + 1];
                    messageIDs[j] = messageIDs[j + 1];
                }
                storedMessages[storedCount - 1] = null;
                messageHashes[storedCount - 1] = null;
                messageIDs[storedCount - 1] = null;
                storedCount--;
                return "Message: \"" + deletedContent + "\" successfully deleted.\n";
            }
        }
        return "Message hash " + hash + " not found.\n";
    }

    public static String displayFullReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("==================== FULL MESSAGE REPORT ====================\n");
        sb.append("Total Sent: ").append(sentCount).append("\n");
        sb.append("Total Disregarded: ").append(disregardedCount).append("\n");
        sb.append("Total Stored: ").append(storedCount).append("\n\n");

        sb.append("--- All Sent Messages ---\n");
        for (int i = 0; i < sentCount; i++) {
            sb.append("ID: ").append(sentMessages[i].getMessageId())
              .append(" | Hash: ").append(sentMessages[i].getMessageHash())
              .append(" | Recipient: ").append(sentMessages[i].getRecipient())
              .append(" | Message: ").append(sentMessages[i].getContent()).append("\n");
        }

        sb.append("\n--- All Stored Messages ---\n");
        for (int i = 0; i < storedCount; i++) {
            sb.append("ID: ").append(storedMessages[i].getMessageId())
              .append(" | Hash: ").append(storedMessages[i].getMessageHash())
              .append(" | Recipient: ").append(storedMessages[i].getRecipient())
              .append(" | Message: ").append(storedMessages[i].getContent()).append("\n");
        }
        sb.append("=============================================================\n");
        return sb.toString();
    }

    public static String loadFromJSON() {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("messages.json")) {
            Object obj = parser.parse(reader);
            JSONArray array = (JSONArray) obj;
            storedCount = 0;
            for (int i = 0; i < array.size() && i < storedMessages.length; i++) {
                JSONObject jsonMsg = (JSONObject) array.get(i);
                String recipient = (String) jsonMsg.get("recipient");
                String content = (String) jsonMsg.get("content");
                storedMessages[i] = new Message(recipient, content);
                storedCount++;
            }
            return "JSON file loaded successfully into array.\n";
        } catch (IOException | ParseException e) {
            return "Error loading JSON: " + e.getMessage() + "\n";
        }
    }

    public static Message[] getSentMessages() {
        Message[] result = new Message[sentCount];
        for (int i = 0; i < sentCount; i++) {
            result[i] = sentMessages[i];
        }
        return result;
    }

    public static int getSentCount() { return sentCount; }
    public static int getStoredCount() { return storedCount; }

    public static void resetArrays() {
        sentCount = 0;
        disregardedCount = 0;
        storedCount = 0;
        for (int i = 0; i < 100; i++) {
            sentMessages[i] = null;
            disregardedMessages[i] = null;
            storedMessages[i] = null;
            messageHashes[i] = null;
            messageIDs[i] = null;
        }
    }
}
   

