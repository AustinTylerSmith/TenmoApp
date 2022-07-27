package services;

import model.Transaction;

import java.util.Arrays;
import java.util.Scanner;

public class ConsoleServices {
    private final Scanner scanner = new Scanner(System.in);
    private final AccountServices accountServices = new AccountServices();
    private final UserServices userServices = new UserServices();
    private final TransactionServices transactionServices = new TransactionServices();

    public void printMainMenu() {
        System.out.println("MAIN MENU");
        System.out.println("Select (1) to login");
        System.out.println("Select (2) to send TE Bucks");
        System.out.println("Select (3) to view transactions");
        System.out.println("Select (4) for Transfer Details");
        System.out.println("Select (5) to request TE Bucks");
        System.out.println("Select (6) to view accept and reject Pending Transfers");
        System.out.println("REGISTER NEW USER (7).");
    }

    public void printSendBucksMenu(String userName) {
        System.out.println("----------------------------");
        System.out.println("Users\nID" + "      Name");
        System.out.println("----------------------------");

        userServices.getUserIdsAndName(userName);
        System.out.println("--------------------------------------------");

    }

    public void printPendingTransactions() {
        System.out.println("-----------------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID           To             Amount");
        System.out.println("-----------------------------------");
    }

    public void printApproveRejectMenu() {
        System.out.println("1: Approve\n2: Reject\n0: Don't approve or reject\n ---------");
    }

    public int promptMenuSelection() {
        int menuSelection = Integer.parseInt(scanner.nextLine());
        return menuSelection;
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public void printErrorMessage(String prompt) {
        System.out.println(prompt);
    }

}
