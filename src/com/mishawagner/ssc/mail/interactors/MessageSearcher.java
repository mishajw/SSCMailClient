package com.mishawagner.ssc.mail.interactors;

import com.mishawagner.ssc.mail.Mail;
import com.mishawagner.ssc.mail.util.MessageUtils;

import javax.mail.Message;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by misha on 04/11/15.
 *
 * Allows user to search for messages
 */
public class MessageSearcher extends MailSearchInteractor {

    public MessageSearcher(Mail mail, Scanner input) {
        super(mail, input);
    }

    @Override
    public void start() {
        List<String> keywords = getKeywordsFromUser();
        System.out.println("Keywords entered: " + Arrays.toString(keywords.toArray()));

        List<Message> foundMessages = searchMessagesByKeyword(mail.getMessages(), keywords);

        System.out.println("\nResults found:");
        foundMessages.forEach(MessageUtils::printSimpleDetails);
    }
}
