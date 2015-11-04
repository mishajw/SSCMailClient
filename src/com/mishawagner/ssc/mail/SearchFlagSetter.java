package com.mishawagner.ssc.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
/**
 * Created by misha on 04/11/15.
 *
 * Allows users to set flags for multiple messages
 */
public class SearchFlagSetter extends MailSearchInteractor {
    private static final Logger __logger = LoggerFactory.getLogger(SearchFlagSetter.class);

    public SearchFlagSetter(Mail mail, Scanner input) {
        super(mail, input);
    }

    @Override
    public void start() {
        List<String> keywords = getKeywordsFromUser();
        System.out.println("Keywords entered: " + Arrays.toString(keywords.toArray()));

        List<Message> foundMessages = searchMessagesByKeyword(mail.getMessages(), keywords);

        System.out.println("\nResults found:");
        foundMessages.forEach(MessageUtils::printSimpleDetails);

        setFlagsForMessages(foundMessages);
    }

    private void setFlagsForMessages(List<Message> messages) {
        Flags.Flag flag;
        boolean result;

        System.out.println("What flag would you like to edit?");
        System.out.println("1) SEEN\n2) RECENT\n3) FLAGGED");
        System.out.print("Enter your option: ");
        int option = Integer.parseInt(input.nextLine());

        switch(option) {
            case 1:
                flag = Flags.Flag.SEEN;
                break;
            case 2:
                flag = Flags.Flag.RECENT;
                break;
            case 3:
                flag = Flags.Flag.FLAGGED;
                break;
            default:
                System.out.println("Not a valid flag.");
                return;
        }

        System.out.println("What would you like to change it to?");
        System.out.println("1) True\n2) False");
        option = Integer.parseInt(input.nextLine());
        result = option == 1;

        for (Message m : messages) {
            try {
                m.setFlag(flag, result);
            } catch (MessagingException e) {
                __logger.error("Couldn't set flag", e);
            }
        }
    }
}
