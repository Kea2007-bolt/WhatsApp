/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.whatsaap;
import java.util.Scanner;

/**
 *
 * @author Student
 */


public class WhatsAppApp {
    private static User registeredUser;
    private static final Scanner scanner = new Scanner(System.in);

    public static boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    public static boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasUpper = false, hasDigit = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasUpper && hasDigit && hasSpecial;
    }

    public static String registerUser(String username, String password, String cell,
                                       String firstName, String lastName) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!Message.checkRecipientCell(cell)) {
            return Message.getRecipientCellFeedback(cell);
        }
        registeredUser = new User(username, password, cell, firstName, lastName);
        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
    }

    public static boolean loginUser(String username, String password) {
        return registeredUser != null &&
               registeredUser.getUsername().equals(username) &&
               registeredUser.getPassword().equals(password);
    }

    public static String returnLoginStatus(boolean success) {
        if (success) {
            return "Welcome to QuickChat.";
        }
        return "Username or password incorrect, please try again.";
    }

    // ✅ THIS IS THE MAIN METHOD — Java needs this to run your program
    public static void main(String[] args) {
        System.out.println("===== QuickChat Messaging System =====\n");

        System.out.println("--- Create Account ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Cell number (+27...): ");
        String cell = scanner.nextLine().trim();
        System.out.print("First Name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine().trim();

        String regResult = registerUser(username, password, cell, firstName, lastName);
        System.out.println("\n" + regResult + "\n");
        if (registeredUser == null) {
            System.out.println("Registration failed. Exiting.");
            return;
        }

        System.out.println("--- Login ---");
        System.out.print("Username: ");
        String loginUser = scanner.nextLine().trim();
        System.out.print("Password: ");
        String loginPass = scanner.nextLine().trim();
        boolean loginSuccess = loginUser(loginUser, loginPass);
        System.out.println("\n" + returnLoginStatus(loginSuccess) + "\n");
        if (!loginSuccess) return;

        while (true) {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    int numMessages = 0;
                    while (true) {
                        System.out.print("How many messages do you want to send? ");
                        try {
                            numMessages = Integer.parseInt(scanner.nextLine().trim());
                            if (numMessages > 0) break;
                            System.out.println("Please enter a positive number.");
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input — please enter a number.");
                        }
                    }

                    for (int i = 1; i <= numMessages; i++) {
                        System.out.println("\n--- Message " + i + " of " + numMessages + " ---");

                        String recipient;
                        while (true) {
                            System.out.print("Recipient cell number (+27...): ");
                            recipient = scanner.nextLine().trim();
                            if (Message.checkRecipientCell(recipient)) break;
                            System.out.println(Message.getRecipientCellFeedback(recipient));
                        }

                        String content;
                        while (true) {
                            System.out.print("Message (max 250 chars): ");
                            content = scanner.nextLine().trim();
                            String check = Message.checkMessageLength(content);
                            System.out.println(check);
                            if (check.contains("ready")) break;
                        }

                        Message msg = new Message(recipient, content);
                        System.out.print("[Send/Discard/Store]: ");
                        String action = scanner.nextLine().trim();
                        String result = msg.sendMessage(action);
                        System.out.println(result);

                        System.out.println("\n--- Message Details ---");
                        System.out.println("Message ID: " + msg.getMessageId());
                        System.out.println("Message Hash: " + msg.getMessageHash());
                        System.out.println("Recipient: " + msg.getRecipient());
                        System.out.println("Message: " + msg.getContent());
                    }
                    System.out.println("\nTotal messages created: " + Message.getTotalMessagesCreated());
                    break;

                case "2":
                    System.out.println("\n===== PART 3 OPTIONS =====");
                    System.out.println("a) Display all stored messages");
                    System.out.println("b) Display longest message");
                    System.out.println("c) Search message by ID");
                    System.out.println("d) Search messages by recipient");
                    System.out.println("e) Delete message by Hash");
                    System.out.println("f) Load messages from JSON");
                    System.out.println("g) Display full report");
                    System.out.println("h) Back to main menu");
                    System.out.print("Choose option: ");
                    
                    String subChoice = scanner.nextLine().trim().toLowerCase();
                    switch(subChoice) {
                        case "a": System.out.println(MessageStore.displayAllStored()); break;
                        case "b": System.out.println(MessageStore.displayLongestMessage()); break;
                        case "c":
                            System.out.print("Enter Message ID: ");
                            String id = scanner.nextLine().trim();
                            System.out.println(MessageStore.searchByMessageId(id));
                            break;
                        case "d":
                            System.out.print("Enter recipient number (+27...): ");
                            String num = scanner.nextLine().trim();
                            System.out.println(MessageStore.searchByRecipient(num));
                            break;
                        case "e":
                            System.out.print("Enter Message Hash to delete: ");
                            String hash = scanner.nextLine().trim();
                            System.out.println(MessageStore.deleteByHash(hash));
                            break;
                        case "f": System.out.println(MessageStore.loadFromJSON()); break;
                        case "g": System.out.println(MessageStore.displayFullReport()); break;
                        case "h": break;
                        default: System.out.println("Invalid option.");
                    }
                    break;

                case "3":
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println("Invalid choice — please try again.");
            }
        }
    }
}

            
        
    


    

