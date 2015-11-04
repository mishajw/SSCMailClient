package com.mishawagner.ssc.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by misha on 04/11/15.
 *
 * View all emails
 */
public class FolderViewer extends MailInteractor {
    private static final Logger __logger = LoggerFactory.getLogger(FolderViewer.class);

    public FolderViewer(Mail mail, Scanner input) {
        super(mail, input);
    }

    @Override
    public void start() {
        System.out.println("\n\n\n");

        Message[] messages = mail.getMessages();

        System.out.println("Enter the index of an email to perform actions on it, or -1 to exit.");
        printFolder(messages);

        System.out.print("Enter your option: ");
        int option = Integer.parseInt(input.nextLine());

        if (option != -1) {
            if (option >= messages.length + 1 || option < 1) {
                System.out.println("Not a valid email index.");
                start();
                return;
            }

            Message chosenMessage = messages[option - 1];
            MessageViewer messageViewer = new MessageViewer(mail, input, chosenMessage);
            messageViewer.start();
        }
    }

    /**
     * Print a folder
     * @param messages list of messages to print
     */
    public void printFolder(Message[] messages) {
        for (int i = 0; i < messages.length; i++) {
            Message m = messages[i];

            System.out.println("Message " + (i + 1) + ":");
            MessageUtils.printSimpleDetails(m);
        }
    }
}
