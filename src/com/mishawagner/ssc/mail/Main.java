package com.mishawagner.ssc.mail;

import java.util.Scanner;

/**
 * Created by misha on 04/11/15.
 *
 * Runs the program
 */
public class Main {
    /**
     * Menu to be displayed to the user
     */
    public static final String mainMenu =
            "Select an option:\n" +
            "1) View all emails\n" +
            "2) Send an email\n" +
            "3) Search for an email\n" +
            "4) Set flags by keywords\n" +
            "5) Exit";

    /**
     * The mail interaction
     */
    private final Mail mail;

    /**
     * How to interact with the user
     */
    private Scanner input;

    /**
     * Start interacting with user
     */
    public Main() {
        this.mail = new Mail();
        this.input = new Scanner(System.in);

        offerMainMenu();
    }

    /**
     * Display the menu to the user and ask for interaction
     */
    private void offerMainMenu() {
        System.out.println("\n\n\n");

        mail.getMessages();

        System.out.println(mainMenu);
        System.out.print("Enter your option: ");
        int option = Integer.parseInt(this.input.next());

        MailInteractor interactor;

        switch (option) {
            case 1:
                interactor = new FolderViewer(this.mail, this.input);
                break;
            case 5:
                System.out.println("Goodbye!");
                return;
            default:
                System.out.println("Not a valid option.");
                offerMainMenu();
                return;
        }

        interactor.start();

        offerMainMenu();
    }

    public static void main(String[] args) {
        new Main();
    }
}
