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
    private static boolean isLoggedIn = false;
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
        String cellResult = Message.checkRecipientCell(cell);
        if (!cellResult.contains("successfully")) return cellResult;
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
            isLoggedIn = true;
            return "Welcome " + registeredUser.getFirstName() + " " +
                   registeredUser.getLastName() + ", it is great to see you again.";
        }
        isLoggedIn = false;
        return "Username or password incorrect, please try again.";
    }

    private static void sendNewMessage() {
        if (!isLoggedIn) {
            System.out.println("You must log in first!");
            return;
        }
        System.out.print("Recipient cell number (+27...): ");
        String recipient = scanner.nextLine().trim();
        String cellCheck = Message.checkRecipientCell(recipient);
        System.out.println(cellCheck);
        if (!cellCheck.contains("successfully")) return;

        System.out.print("Enter your message (max 250 characters): ");
        String messageText = scanner.nextLine().trim();
        String lengthCheck = Message.checkMessageLength(messageText);
        System.out.println(lengthCheck);
        if (!lengthCheck.contains("ready")) return;

        Message newMessage = new Message(recipient, messageText);
        System.out.println("Message ID generated: " + newMessage.getMessageId());
        System.out.println("Message Hash: " + newMessage.getMessageHash());

        System.out.print("[Send/Discard/Store]: ");
        String action = scanner.nextLine().trim();
        System.out.println(newMessage.sendMessage(action));
    }

    public static void main(String[] args) {
        System.out.println("===== WhatsApp — Complete System =====\n");

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
            System.out.println("2) Show sent messages");
            System.out.println("3) Total messages sent");
            System.out.println("4) ----- PART 3: STORED MESSAGES -----");
            System.out.println("5) Display all stored messages");
            System.out.println("6) Display longest stored message");
            System.out.println("7) Search message by ID");
            System.out.println("8) Search messages by recipient");
            System.out.println("9) Delete message by Hash");
            System.out.println("10) Load from JSON into array");
            System.out.println("11) Display FULL Report");
            System.out.println("12) Exit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": sendNewMessage(); break;
                case "2": System.out.println(Message.printMessages()); break;
                case "3": System.out.println("Total sent: " + Message.returnTotalMessages()); break;
                case "5": System.out.println(MessageStore.displayAllStored()); break;
                case "6": System.out.println(MessageStore.displayLongestMessage()); break;
                case "7":
                    System.out.print("Enter Message ID to search: ");
                    String id = scanner.nextLine().trim();
                    System.out.println(MessageStore.searchByMessageId(id));
                    break;
                case "8":
                    System.out.print("Enter recipient number: ");
                    String num = scanner.nextLine().trim();
                    System.out.println(MessageStore.searchByRecipient(num));
                    break;
                case "9":
                    System.out.print("Enter Message Hash to delete: ");
                    String hash = scanner.nextLine().trim();
                    System.out.println(MessageStore.deleteByHash(hash));
                    break;
                case "10": System.out.println(MessageStore.loadFromJSON()); break;
                case "11": System.out.println(MessageStore.displayFullReport()); break;
                case "12":
                    System.out.println("Exiting... Goodbye!");
                    return;
                default: System.out.println("Invalid option. Try again.");
            }
        }
    }
}
            
        
    


    

