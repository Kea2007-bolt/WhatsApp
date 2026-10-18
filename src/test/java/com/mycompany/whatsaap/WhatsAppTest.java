/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.whatsaap;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author Student
 */
public class WhatsAppTest {
    
  





    @Test
    public void testUsernameCorrectFormat() {
        assertTrue(WhatsAppApp.checkUserName("kyl_1"));
    }

    @Test
    public void testUsernameIncorrectFormat() {
        assertFalse(WhatsAppApp.checkUserName("kyle!!!!!!"));
    }

    @Test
    public void testPasswordMeetsComplexity() {
        assertTrue(WhatsAppApp.checkPasswordComplexity("Ch&sec@k99"));
    }

    @Test
    public void testPasswordFailsComplexity() {
        assertFalse(WhatsAppApp.checkPasswordComplexity("password"));
    }

    @Test
    public void testRecipientCellCorrect() {
        String result = Message.checkRecipientCell("+27186930002");
        assertTrue(result.contains("successfully captured"));
    }

    @Test
    public void testRecipientCellIncorrect() {
        String result = Message.checkRecipientCell("08575975889");
        assertTrue(result.contains("incorrectly formatted"));
    }

    @Test
    public void testMessageLengthOk() {
        String result = Message.checkMessageLength("Hi Mike, can you join us for dinner tonight?");
        assertTrue(result.contains("ready to send"));
    }

    @Test
    public void testMessageTooLong() {
        String longMsg = "a".repeat(251);
        String result = Message.checkMessageLength(longMsg);
        assertTrue(result.contains("exceeds 250"));
    }

    @Test
    public void testMessageIdValid() {
        assertTrue(Message.checkMessageID("1234567890"));
    }

    @Test
    public void testMessageIdTooLong() {
        assertFalse(Message.checkMessageID("12345678901"));
    }

    @Test
    public void testMessageHashFormat() {
        Message m = new Message("+27186930002", "Hi tonight");
        String hash = m.createMessageHash();
        assertTrue("Hash format: 00:1:FIRSTLAST", hash.matches("\\d{2}:\\d+:[A-Z]+"));
    }

    @Test
    public void testSendOptions() {
        Message m = new Message("+27186930002", "Test");
        assertTrue(m.sendMessage("send").contains("successfully sent"));
        assertTrue(m.sendMessage("discard").contains("delete"));
        assertTrue(m.sendMessage("store").contains("successfully stored"));
    }
}

