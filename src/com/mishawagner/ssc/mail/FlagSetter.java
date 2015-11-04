package com.mishawagner.ssc.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.Scanner;

/**
 * Created by misha on 04/11/15.
 *
 * Allows user to set flags
 */
public class FlagSetter extends MailInteractor {
    private static final Logger __logger = LoggerFactory.getLogger(FlagSetter.class);

    private Message message;

    public FlagSetter(Mail mail, Scanner input, Message message) {
        super(mail, input);
        this.message = message;
    }

    @Override
    public void start() {
        System.out.printf("\n\n\n");
        printMainMenu();
        System.out.print("Enter your option: ");
        int option = Integer.parseInt(input.next());

        try {
            switch (option) {
                case 1:
                    message.setFlag(Flags.Flag.SEEN, !message.isSet(Flags.Flag.SEEN));
                    break;
                case 2:
                    message.setFlag(Flags.Flag.RECENT, !message.isSet(Flags.Flag.RECENT));
                    break;
                case 3:
                    message.setFlag(Flags.Flag.FLAGGED, !message.isSet(Flags.Flag.FLAGGED));
                    break;
                default:
                    System.out.println("Not a supported option.");
                    this.start();
                    return;
            }
        } catch (MessagingException e) {
            __logger.error("Couldn't edit flags", e);
        }

        System.out.println("Done.");
    }

    /**
     * Print the main message to display to the user
     */
    public void printMainMenu() {
        System.out.println("What flag would you like to toggle?");
        try {
            System.out.println("1) SEEN: " + this.message.isSet(Flags.Flag.SEEN));
            System.out.println("2) RECENT: " + this.message.isSet(Flags.Flag.RECENT));
            System.out.println("3) FLAGGED: " + this.message.isSet(Flags.Flag.FLAGGED));
        } catch (MessagingException e) {
            __logger.error("Couldn't read message", e);
        }
    }
}
