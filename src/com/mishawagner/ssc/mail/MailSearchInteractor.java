package com.mishawagner.ssc.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * Created by misha on 04/11/15.
 *
 * An interactor that has capabilities to search for messages too
 */
public abstract class MailSearchInteractor extends MailInteractor {
    private static final Logger __logger = LoggerFactory.getLogger(MailSearchInteractor.class);

    public MailSearchInteractor(Mail mail, Scanner input) {
        super(mail, input);
    }

    protected List<Message> searchMessagesByKeyword(Message[] messages, List<String> keywords) {
        List<Message> filtered = new ArrayList<>();

        for (Message m : messages) {
            if (checkHasKeywords(m, keywords)) {
                filtered.add(m);
            }
        }

        return filtered;
    }

    protected boolean checkHasKeywords(Message m, List<String> keywords) {
        String wholeText = "";

        try {
            wholeText += m.getSubject();
            wholeText += MessageUtils.getMainBody(m);
        } catch (MessagingException e) {
            __logger.info("Couldn't read message", e);
        }

        for (String kw : keywords) {
            if (wholeText.contains(kw)) {
                return true;
            }
        }

        return false;
    }

    protected List<String> getKeywordsFromUser() {
        List<String> keywords = new ArrayList<>();
        String curKeyword;

        do {
            System.out.print("Add a keyword to search for, leave empty to finish: ");
            curKeyword = input.nextLine();

            if (!curKeyword.equals("")) {
                keywords.add(curKeyword);
            }
        } while (!curKeyword.equals(""));

        return keywords;
    }
}
