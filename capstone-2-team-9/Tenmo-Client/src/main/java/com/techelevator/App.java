package com.techelevator;

import model.RegisterUserDTO;
import model.Transaction;
import model.User;
import services.*;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private final AccountServices accountServices = new AccountServices();
    public AuthenticationService authenticationService = new AuthenticationService();
    private final TransactionServices transactionServices = new TransactionServices();
    private final UserServices userServices = new UserServices();
    private final ConsoleServices consoleServices = new ConsoleServices();
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        int infinityLoop = -1;
        String menuSelection;
        while (infinityLoop != 0) {
            consoleServices.printMainMenu();
            menuSelection = scanner.nextLine();
            if (menuSelection.equalsIgnoreCase("1")) {
                if (handleLogin()) {
                    BigDecimal balance = accountServices.listAccountByUserId(Integer.parseInt(userServices.getUserIdByName(authenticationService.getCredentialsDtoUsername()))).getBalance();
                    System.out.println("Your current balance: $" + balance);
                } else {
                    System.out.println("Invalid login please try again");
                }
            } else if (menuSelection.equalsIgnoreCase("2")) {
                handleSendTeBucks();
            } else if (menuSelection.equalsIgnoreCase("3")) {
                handleViewTransactions();
            } else if (menuSelection.equalsIgnoreCase("4")) {
                handleTransferDetails();
            } else if (menuSelection.equalsIgnoreCase("5")) {
                handleRequestTeBucks();
            } else if (menuSelection.equalsIgnoreCase("6")) {
                handlePendingRequest();
            } else if (menuSelection.equalsIgnoreCase("7")) {
                handleRegisterNewUser();
            }
        }
    }

    private boolean handleLogin() {
        String username = consoleServices.promptForString("Username: ");
        String password = consoleServices.promptForString("Password: ");
        String token = authenticationService.login(username, password);
        if (token != null) {
            accountServices.setAuthToken(token);
            transactionServices.setAuthToken(token);
            userServices.setAuthToken(token);
            return true;
        } else {
            consoleServices.printErrorMessage("login failed");
            return false;
        }
    }

    private void handleSendTeBucks() {
        consoleServices.printSendBucksMenu(authenticationService.getCredentialsDtoUsername());
        String sendMoneyToUserId = consoleServices.promptForString("Enter the Id of the user you are sending to (0 to cancel): ");
        User[] usersToSend = userServices.listUsersWithFriendlyInfo();
        List<String> ids = new ArrayList<>();
        for (User checkUser : usersToSend) {
            ids.add(checkUser.getId().toString());
        }
        if (!ids.contains(sendMoneyToUserId)) {
            System.out.println("This user does not exist please try again.");
        } else {
            String amountToSend = consoleServices.promptForString("Enter amount:");
            try {
                Transaction transaction = new Transaction();
                int recipient = accountServices.listAccountByUserId(Integer.parseInt(sendMoneyToUserId)).getAccountId();
                int userId = Integer.parseInt(userServices.getUserIdByName(authenticationService.getCredentialsDtoUsername()));
                int sender = accountServices.listAccountByUserId(userId).getAccountId();
                transaction.setAmount(new BigDecimal(amountToSend));
                transaction.setRecipientId(recipient);
                transaction.setSenderId(sender);
                if (accountServices.listAccountByUserId(userId).getBalance().compareTo(transaction.getAmount()) < 0) {
                    System.out.println("You do not have the appropriate funds to complete this transaction.");
                } else {
                    transactionServices.addTransaction(transaction);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }

    private void handleViewTransactions() {
        int userId = Integer.parseInt(userServices.getUserIdByName(authenticationService.getCredentialsDtoUsername()));
        int sender = accountServices.listAccountByUserId(userId).getAccountId();
        Transaction[] transactions = null;
        transactions = transactionServices.listAccountTransactions(sender);
        System.out.println("--------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID          From/To           Amount");
        System.out.println("---------------------------------------");
        for (Transaction transaction : transactions) {
            if (transaction.getStatus().equalsIgnoreCase("Approved")) {
                if (transaction.getRecipientId() == sender) {
                    System.out.println(transaction.getTransactionId() + "           From: " + userServices.getNameByUserId(accountServices.recieveUserIdForAccount(transaction.getSenderId())) + "            $" + transaction.getAmount());
                } else {
                    System.out.println(transaction.getTransactionId() + "           To: " + userServices.getNameByUserId(accountServices.recieveUserIdForAccount(transaction.getRecipientId())) + "           $" + transaction.getAmount());
                }
            }
        }
    }


    private void handleTransferDetails() {
        int userId = Integer.parseInt(userServices.getUserIdByName(authenticationService.getCredentialsDtoUsername()));
        int sender = accountServices.listAccountByUserId(userId).getAccountId();
        Transaction[] transactions = null;
        transactions = transactionServices.listAccountTransactions(sender);
        System.out.println("--------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("--------------------------------------------");
        for (Transaction transaction : transactions) {
            System.out.println("Id: " + transaction.getTransactionId() + "\n" + "From: " + userServices.getNameByUserId(accountServices.recieveUserIdForAccount(transaction.getSenderId())) + "\n" +
                    "To: " + userServices.getNameByUserId(accountServices.recieveUserIdForAccount(transaction.getRecipientId())) + "\n" +
                    "Status: " + transaction.getStatus() + "\n" + "Amount: $" + transaction.getAmount());
            System.out.println("------------------------------------------------");
        }
    }

    private void handleRequestTeBucks() {
        consoleServices.printSendBucksMenu(authenticationService.getCredentialsDtoUsername());
        String sendMoneyToUserId = consoleServices.promptForString("Enter the Id of the user you are requesting from (0 to cancel): ");
        User[] usersToSend = userServices.listUsersWithFriendlyInfo();
        List<String> ids = new ArrayList<>();
        for (User checkUser : usersToSend) {
            ids.add(checkUser.getId().toString());
        }
        if (!ids.contains(sendMoneyToUserId)) {
            System.out.println("This user does not exist please try again.");
        } else {
            String amountToSend = consoleServices.promptForString("Enter amount:");
            try {
                Transaction transaction = new Transaction();
                int sender = accountServices.listAccountByUserId(Integer.parseInt(sendMoneyToUserId)).getAccountId();
                int userId = Integer.parseInt(userServices.getUserIdByName(authenticationService.getCredentialsDtoUsername()));
                int recipient = accountServices.listAccountByUserId(userId).getAccountId();
                transaction.setAmount(new BigDecimal(amountToSend));
                transaction.setRecipientId(recipient);
                transaction.setSenderId(sender);
                transactionServices.addTransaction(transaction);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount");
            }
        }
    }

    private void handlePendingRequest() {
        int userId = Integer.parseInt(userServices.getUserIdByName(authenticationService.getCredentialsDtoUsername()));
        int currentUserAccount = accountServices.listAccountByUserId(userId).getAccountId();
        consoleServices.printPendingTransactions();
        Transaction[] pending = transactionServices.listPendingAccountTransactions(currentUserAccount);
        for (Transaction transaction : pending) {
            System.out.println(transaction.getTransactionId() + "        " + userServices.getNameByUserId(accountServices.recieveUserIdForAccount(transaction.getRecipientId()))
                    + "       $" + transaction.getAmount());
        }
        System.out.println("-------------------------------------------");
        System.out.println("Please enter transfer ID to approve/reject (0 to cancel): ");
        String IDofTransfer = scanner.nextLine();
        for (Transaction transaction : pending) {
            if (IDofTransfer.equalsIgnoreCase(String.valueOf(transaction.getTransactionId()))) {
                consoleServices.printApproveRejectMenu();
                System.out.println("Please choose an option: ");
                String selection = scanner.nextLine();
                if (selection.equalsIgnoreCase("1")) {
                    transactionServices.approveTransaction(Integer.parseInt(IDofTransfer));
                } else if (selection.equalsIgnoreCase("2")) {
                    transactionServices.rejectTransaction(Integer.parseInt(IDofTransfer));
                } else if (selection.equalsIgnoreCase("0")) {
                    continue;
                }
            } else {
                continue;
            }
        }

    }

    private void handleRegisterNewUser() {
        System.out.println("Welcome Please enter the appropriate info below to register as a new user.");
        String username = consoleServices.promptForString("Username: ");
        String password = consoleServices.promptForString("Password: ");
        RegisterUserDTO newUser = new RegisterUserDTO();
        newUser.setUsername(username);
        newUser.setPassword(password);
        authenticationService.register(newUser);
        System.out.println("Thank you " + username + " you have been registered.");
    }


}
