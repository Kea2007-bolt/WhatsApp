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

    // Reset ALL counters and arrays BEFORE every single test
    @Before
    public void setUp() {
        Message.resetTotalCounter();
        MessageStore.resetArrays();
    }

    // =====================================================
    // ========== PART 1 — Login & Registration Tests ======
    // =====================================================

    @Test
    public void testUsernameCorrect_FormatAccepted() {
        assertTrue("Valid username should pass", WhatsAppApp.checkUserName("kyl_1"));
    }

    @Test
    public void testUsernameIncorrect_NoUnderscore_Fails() {
        assertFalse("Username without underscore should fail", WhatsAppApp.checkUserName("kyle"));
    }

    @Test
    public void testUsernameIncorrect_TooLong_Fails() {
        assertFalse("Username over 5 chars should fail", WhatsAppApp.checkUserName("kyle1234"));
    }

    @Test
    public void testPassword_MeetsAllRequirements_Passes() {
        assertTrue("Valid password should pass", WhatsAppApp.checkPasswordComplexity("Passw0rd!"));
    }

    @Test
    public void testPassword_TooShort_Fails() {
        assertFalse("Short password should fail", WhatsAppApp.checkPasswordComplexity("Pass1!"));
    }

    @Test
    public void testPassword_NoUppercase_Fails() {
        assertFalse("No uppercase letter should fail", WhatsAppApp.checkPasswordComplexity("passw0rd!"));
    }

    @Test
    public void testPassword_NoNumber_Fails() {
        assertFalse("No digit should fail", WhatsAppApp.checkPasswordComplexity("Password!"));
    }

    @Test
    public void testPassword_NoSpecialChar_Fails() {
        assertFalse("No special character should fail", WhatsAppApp.checkPasswordComplexity("Password1"));
    }

    @Test
    public void testRegistration_SuccessMessage() {
        String result = WhatsAppApp.registerUser("joh_d", "Test123!", "+27831234567", "John", "Doe");
        assertTrue("Should contain success messages",
            result.contains("Username successfully captured") &&
            result.contains("Password successfully captured"));
    }

    @Test
    public void testLogin_CorrectCredentials_ReturnsWelcome() {
        WhatsAppApp.registerUser("tes_t", "Pass123!", "+27830000000", "Test", "User");
        boolean success = WhatsAppApp.loginUser("tes_t", "Pass123!");
        assertTrue("Login should succeed with correct details", success);
        assertEquals("Welcome to QuickChat.", WhatsAppApp.returnLoginStatus(success));
    }

    @Test
    public void testLogin_WrongPassword_Fails() {
        WhatsAppApp.registerUser("tes_t", "Pass123!", "+27830000000", "Test", "User");
        assertFalse("Login should fail with wrong password",
            WhatsAppApp.loginUser("tes_t", "WrongPass1!"));
    }

    // =====================================================
    // ========== PART 2 — Message & Hash Tests ============
    // =====================================================

    @Test
    public void testMessageLength_Exactly250_Passes() {
        String msg = "x".repeat(250);
        assertEquals("Message ready to send.", Message.checkMessageLength(msg));
    }

    @Test
    public void testMessageLength_OverLimit_Fails() {
        String msg = "x".repeat(255);
        assertTrue("Should state over-limit message",
            Message.checkMessageLength(msg).contains("exceeds 250 characters"));
    }

    @Test
    public void testRecipientCell_ValidFormat_Passes() {
        assertTrue("+27 followed by 9 digits should be valid",
            Message.checkRecipientCell("+27831234567"));
    }

    @Test
    public void testRecipientCell_NoCountryCode_Fails() {
        assertFalse("Number without +27 should fail", Message.checkRecipientCell("0831234567"));
    }

    // ========== EXACT PART 2 TEST DATA — From Assignment ==========

    @Test
    public void testPart2_Data1_HashGenerated_NoPunctuation() {
        Message m = new Message("+27834557896", "Did you get the cake?");
        String hash = m.createMessageHash();
        assertNotNull("Hash should not be null", hash);
        assertFalse("Hash must NOT contain punctuation", hash.matches(".*[?:!.,;].*"));
        System.out.println("✓ Test Data 1 Hash: " + hash);
    }

    @Test
    public void testPart2_Data2_HashGenerated_NoPunctuation() {
        Message m = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        String hash = m.createMessageHash();
        assertNotNull("Hash should not be null", hash);
        assertFalse("Hash must NOT contain punctuation", hash.matches(".*[?:!.,;].*"));
        System.out.println("✓ Test Data 2 Hash: " + hash);
    }

    @Test
    public void testPart2_Data3_HashGenerated_NoPunctuation() {
        Message m = new Message("+27834484567", "Yahoooo, I am at your gate.");
        String hash = m.createMessageHash();
        assertNotNull("Hash should not be null", hash);
        assertFalse("Hash must NOT contain punctuation", hash.matches(".*[?:!.,;].*"));
        System.out.println("✓ Test Data 3 Hash: " + hash);
    }

    @Test
    public void testPart2_Data4_InvalidCell_FeedbackMessage() {
        String feedback = Message.getRecipientCellFeedback("0838884567");
        assertTrue("Should tell user to correct format", feedback.contains("incorrectly formatted"));
    }

    // ========== HASH LOOP TEST — Grader Requirement ==========

    @Test
    public void testMessageHash_LoopTest_MultipleMessages_UniqueCleanHashes() {
        String[] recipients = {"+27830000001", "+27830000002", "+27830000003", "+27830000004", "+27830000005"};
        String[] messages = {
            "First message here.",
            "Second message — testing!",
            "Third message sent.",
            "Message number four is great.",
            "And finally the fifth message."
        };

        String[] generatedHashes = new String[5];

        for (int i = 0; i < 5; i++) {
            Message m = new Message(recipients[i], messages[i]);
            generatedHashes[i] = m.createMessageHash();

            assertNotNull("Hash " + (i+1) + " should exist", generatedHashes[i]);
            assertFalse("Hash " + (i+1) + " has NO punctuation",
                generatedHashes[i].matches(".*[?:!.,;].*"));
            System.out.println("✓ Hash Loop Test " + (i+1) + ": " + generatedHashes[i]);
        }

        // Verify all hashes are UNIQUE
        assertNotEquals("Hash 1 ≠ Hash 2", generatedHashes[0], generatedHashes[1]);
        assertNotEquals("Hash 2 ≠ Hash 3", generatedHashes[1], generatedHashes[2]);
        assertEquals("Total messages counter = 5", 5, Message.getTotalMessagesCreated());
    }

    @Test
    public void testTotalCounter_CountsEveryMessageCreated() {
        assertEquals("Starts at zero", 0, Message.getTotalMessagesCreated());
        new Message("+27111111111", "Message one");
        new Message("+27222222222", "Message two");
        new Message("+27333333333", "Message three");
        assertEquals("Counts ALL 3 messages", 3, Message.getTotalMessagesCreated());
    }

    // =====================================================
    // ========== PART 3 — Array & MessageStore Tests ======
    // =====================================================

    @Test
    public void testArrays_AddStoredMessage_CountIncreases() {
        Message m = new Message("+27831112222", "Hello arrays!");
        MessageStore.addStoredMessage(m);
        assertEquals("1 message in store", 1, MessageStore.getStoredCount());
    }

    @Test
    public void testDisplayAllStored_MessageExists_ReturnsContent() {
        MessageStore.addStoredMessage(new Message("+27835556666", "Display test message"));
        String result = MessageStore.displayAllStored();
        assertTrue("Shows recipient", result.contains("+27835556666"));
        assertTrue("Shows message text", result.contains("Display test message"));
    }

    @Test
    public void testDisplayAllStored_Empty_ReturnsNoneMessage() {
        assertTrue("Empty store says no messages",
            MessageStore.displayAllStored().contains("No stored messages"));
    }

    @Test
    public void testDisplayLongestMessage_ReturnsLongestOne() {
        MessageStore.addStoredMessage(new Message("+2700000001", "Short"));
        MessageStore.addStoredMessage(new Message("+2700000002", "This message is definitely longer than the first one"));
        String result = MessageStore.displayLongestMessage();
        assertTrue("Identifies longest message", result.contains("definitely longer"));
        assertTrue("Shows character length", result.contains("characters"));
    }

    @Test
    public void testSearchByRecipient_MatchFound_ReturnsResults() {
        MessageStore.addStoredMessage(new Message("+27999887766", "Hi there"));
        MessageStore.addStoredMessage(new Message("+27999887766", "How are you?"));
        MessageStore.addStoredMessage(new Message("+27111223344", "Different person"));

        String result = MessageStore.searchByRecipient("+27999887766");
        assertTrue("Finds both messages", result.contains("Hi there") && result.contains("How are you?"));
        assertTrue("Shows total count", result.contains("Total messages found: 2"));
    }

    @Test
    public void testSearchByRecipient_NoMatch_ReturnsNotFound() {
        MessageStore.addStoredMessage(new Message("+27555555555", "Someone else"));
        String result = MessageStore.searchByRecipient("+27999999999");
        assertTrue("Says no messages found", result.contains("No messages found"));
    }

    @Test
    public void testDeleteByHash_MessageExists_RemovesIt() {
        Message m = new Message("+27444332211", "Delete me please");
        MessageStore.addStoredMessage(m);
        String hashToDelete = m.getMessageHash();

        String result = MessageStore.deleteByHash(hashToDelete);
        assertTrue("Confirms deletion", result.contains("Successfully Deleted"));
        assertEquals("Store is now empty", 0, MessageStore.getStoredCount());
    }

    @Test
    public void testDeleteByHash_NotFound_ReturnsMessage() {
        String result = MessageStore.deleteByHash("INVALIDHASH123");
        assertTrue("Says not found", result.contains("not found"));
    }

    @Test
    public void testSearchByMessageId_Found_ReturnsDetails() {
        Message m = new Message("+27777665544", "Find me by ID");
        MessageStore.addStoredMessage(m);
        String idToFind = m.getMessageId();

        String result = MessageStore.searchByMessageId(idToFind);
        assertTrue("Message found in results", result.contains("Find me by ID"));
    }

    @Test
    public void testDisplayFullReport_IncludesAllStats() {
        MessageStore.addStoredMessage(new Message("+27123456789", "Report test 1"));
        MessageStore.addStoredMessage(new Message("+27987654321", "Report test 2"));
        String report = MessageStore.displayFullReport();
        assertTrue("Shows total created", report.contains("Total Messages Created"));
        assertTrue("Shows stored count", report.contains("Stored Messages"));
        assertTrue("Contains message details", report.contains("Report test"));
    }
} 


