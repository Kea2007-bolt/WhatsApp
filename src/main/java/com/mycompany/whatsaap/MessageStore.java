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
        return "Longest Message:\nRecipient: " + storedMessages[longestIndex].getRecipient() + "\n"
             + "Message: \"" + storedMessages[longestIndex].getContent() + "\"\n"
             + "Length: " + maxLength + " characters\n";
    }

    public static String searchByMessageId(String id) {
        for (int i = 0; i < storedCount; i++) {
            if (messageIDs[i] != null && messageIDs[i].equals(id)) {
                return "Message Found:\nID: " + storedMessages[i].getMessageId() + "\n"
                     + "Recipient: " + storedMessages[i].getRecipient() + "\n"
                     + "Message: " + storedMessages[i].getContent() + "\n";
            }
        }
        return "Message ID " + id + " not found.\n";
    }

    public static String searchByRecipient(String recipientNum) {
        StringBuilder sb = new StringBuilder();
        int matchCount = 0;
        sb.append("=== Messages to: ").append(recipientNum).append(" ===\n");
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].getRecipient().equals(recipientNum)) {
                sb.append("ID: ").append(storedMessages[i].getMessageId())
                  .append(" | Message: ").append(storedMessages[i].getContent()).append("\n");
                matchCount++;
            }
        }
        if (matchCount == 0) return "No messages found for recipient " + recipientNum + ".\n";
        sb.append("Total messages found: ").append(matchCount).append("\n");
        return sb.toString();
    }

    public static String deleteByHash(String hash) {
        for (int i = 0; i < storedCount; i++) {
            if (messageHashes[i] != null && messageHashes[i].equals(hash)) {
                String deletedMsg = storedMessages[i].getContent();
                String deletedRecipient = storedMessages[i].getRecipient();
                for (int j = i; j < storedCount - 1; j++) {
                    storedMessages[j] = storedMessages[j + 1];
                    messageHashes[j] = messageHashes[j + 1];
                    messageIDs[j] = messageIDs[j + 1];
                }
                int last = storedCount - 1;
                storedMessages[last] = null;
                messageHashes[last] = null;
                messageIDs[last] = null;
                storedCount--;
                return "Message Successfully Deleted:\nRecipient: " + deletedRecipient + "\n"
                     + "Message: \"" + deletedMsg + "\"\n";
            }
        }
        return "Message hash " + hash + " not found — nothing deleted.\n";
    }

    public static String displayFullReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("==================== FULL MESSAGE REPORT ====================\n");
        sb.append("Total Messages Created: ").append(Message.getTotalMessagesCreated()).append("\n");
        sb.append("Sent Messages: ").append(sentCount).append("\n");
        sb.append("Disregarded Messages: ").append(disregardedCount).append("\n");
        sb.append("Stored Messages: ").append(storedCount).append("\n\n");
        sb.append("--- Sent Messages ---\n");
        if (sentCount == 0) sb.append("None.\n");
        for (int i = 0; i < sentCount; i++) {
            sb.append("ID: ").append(sentMessages[i].getMessageId())
              .append(" | Hash: ").append(sentMessages[i].getMessageHash())
              .append(" | Recipient: ").append(sentMessages[i].getRecipient())
              .append(" | Message: ").append(sentMessages[i].getContent()).append("\n");
        }
        sb.append("\n--- Stored Messages ---\n");
        if (storedCount == 0) sb.append("None.\n");
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
        int loaded = 0;
        try (FileReader reader = new FileReader("messages.json")) {
            Object obj = parser.parse(reader);
            JSONArray array = (JSONArray) obj;
            storedCount = 0;
            for (int i = 0; i < array.size() && i < storedMessages.length; i++) {
                JSONObject jsonMsg = (JSONObject) array.get(i);
                String recipient = (String) jsonMsg.get("recipient");
                String content = (String) jsonMsg.get("content");
                storedMessages[i] = new Message(recipient, content);
                messageHashes[i] = storedMessages[i].getMessageHash();
                messageIDs[i] = storedMessages[i].getMessageId();
                storedCount++;
                loaded++;
            }
        } catch (IOException | ParseException e) {
            return "No existing file or error reading: " + e.getMessage() + "\n";
        }
        return loaded + " messages loaded from JSON file.\n";
    }

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
   

