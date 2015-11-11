package com.mishawagner.ssc.mail.interactors;

import com.mishawagner.ssc.mail.Mail;
import com.mishawagner.ssc.mail.util.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by misha on 04/11/15.
 *
 * View and perform operations on an image
 */
public class MessageViewer extends MailInteractor{
    private static final Logger __logger = LoggerFactory.getLogger(MessageViewer.class);

    /**
     * The menu displayed to the user
     */
    private static final String mainMenu = "Select an option\n" +
            "1) View message\n" +
            "2) Set a flag\n" +
            "3) Go back";

    /**
     * The message to interact with
     */
    private Message message;

    /**
     * Initialise with a message
     * @param mail the mail interface to interact with
     * @param input how to interact with user
     * @param message message to use
     */
    public MessageViewer(Mail mail, Scanner input, Message message) {
        super(mail, input);

        this.message = message;
    }

    @Override
    public void start() {
        System.out.println("\n\n\n");

        printMessageInfo();
        System.out.println(mainMenu);
        System.out.print("Enter your option: ");
        int option = Integer.parseInt(input.nextLine());

        switch (option) {
            case 1:
                System.out.println(MessageUtils.getAllBody(this.message));
                break;
            case 2:
                MailInteractor flagSetter = new FlagSetter(mail, input, message);
                flagSetter.start();
                break;
            case 3:
                return;
            default:
                System.out.println("Unsupported option, try again.");
                this.start();
                break;
        }
    }

    /**
     * Print information about the message
     */
    private void printMessageInfo() {
        try {
            System.out.println("SUBJECT: " + this.message.getSubject());
            System.out.println("\tFROM: " + Arrays.toString(this.message.getFrom()));
        } catch (MessagingException e) {
            __logger.error("Couldn't read message details", e);
        }
    }
}
