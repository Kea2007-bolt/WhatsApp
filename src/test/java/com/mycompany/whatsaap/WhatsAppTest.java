/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.whatsaap;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author Student
 */
public class WhatsAppTest {
    
  




    @Before
    public void reset() {
        MessageStore.resetArrays();
    }

    @Test public void testUsernameCorrect() { assertTrue(WhatsAppApp.checkUserName("kyl_1")); }
    @Test public void testUsernameIncorrect() { assertFalse(WhatsAppApp.checkUserName("kyle!!!!!!")); }
    @Test public void testPasswordPass() { assertTrue(WhatsAppApp.checkPasswordComplexity("Ch&sec@k99")); }
    @Test public void testPasswordFail() { assertFalse(WhatsAppApp.checkPasswordComplexity("password")); }

    @Test
    public void testRecipientCellCorrect() {
        assertTrue(Message.checkRecipientCell("+27834557896").contains("successfully captured"));
    }

    @Test
    public void testRecipientCellIncorrect() {
        assertTrue(Message.checkRecipientCell("0838884567").contains("incorrectly formatted"));
    }

    @Test
    public void testMessageLengthPass() {
        assertTrue(Message.checkMessageLength("Did you get the cake?").contains("ready to send"));
    }

    @Test
    public void testMessageLengthFail() {
        String longMsg = new String(new char[251]).replace("\0", "a");
        assertTrue(Message.checkMessageLength(longMsg).contains("exceeds 250"));
    }

    @Test
    public void testSentMessagesArrayPopulated() {
        new Message("+27834557896", "Did you get the cake?").sendMessage("send");
        new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.").sendMessage("store");
        new Message("+27834484567", "Yahoooo, I am at your gate.").sendMessage("discard");
        new Message("0838884567", "It is dinner time!").sendMessage("send");

        Message[] sent = MessageStore.getSentMessages();
        assertEquals(2, sent.length);
        assertEquals("Did you get the cake?", sent[0].getContent());
        assertEquals("It is dinner time!", sent[1].getContent());
    }

    @Test
    public void testDisplayLongestMessage() {
        new Message("+27834557896", "Did you get the cake?").sendMessage("store");
        new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.").sendMessage("store");

        String result = MessageStore.displayLongestMessage();
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
    }

    @Test
    public void testSearchByMessageID() {
        Message m = new Message("0838884567", "It is dinner time!");
        m.sendMessage("store");
        String id = m.getMessageId();

        String result = MessageStore.searchByMessageId(id);
        assertTrue(result.contains("It is dinner time!"));
    }

    @Test
    public void testSearchByRecipient() {
        new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.").sendMessage("store");
        new Message("+27838884567", "Ok, I am leaving without you.").sendMessage("store");

        String result = MessageStore.searchByRecipient("+27838884567");
        assertTrue(result.contains("Where are you?"));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteByMessageHash() {
        Message m = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        m.sendMessage("store");
        String hash = m.getMessageHash();

        String result = MessageStore.deleteByHash(hash);
        assertTrue(result.contains("successfully deleted"));
        assertEquals(0, MessageStore.getStoredCount());
    }

    @Test
    public void testDisplayReport() {
        new Message("+27834557896", "Did you get the cake?").sendMessage("send");
        new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.").sendMessage("store");

        String report = MessageStore.displayFullReport();
        assertTrue(report.contains("Message Hash"));
        assertTrue(report.contains("Did you get the cake?"));
    }
}
    


