package com.mishawagner.ssc.mail.interactors;

import com.mishawagner.ssc.mail.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by misha on 04/11/15.
 *
 * Allows users to make messages
 */
public class MessageCreator extends MailInteractor {
    private static final Logger __logger = LoggerFactory.getLogger(MessageCreator.class);

    /**
     * Message details
     */
    private String to, cc, subject, body, attachmentPath;

    public MessageCreator(Mail mail, Scanner input) {
        super(mail, input);
    }

    @Override
    public void start() {
        setMessageDetailsFromUser();
        sendMessage();
    }

    /**
     * Get user to input message details
     */
    private void setMessageDetailsFromUser() {
        System.out.print("To: ");
        this.to = input.nextLine();

        System.out.print("Cc: ");
        this.cc = input.nextLine();

        System.out.print("Subject: ");
        this.subject = input.nextLine();

        System.out.print("Message body: ");
        this.body = input.nextLine();

        System.out.print("Attachment path (leave blank for no attachment): ");
        this.attachmentPath = input.nextLine();
    }


    /**
     * Send a default message for testing purposes
     */
    @SuppressWarnings("unused")
    private void setMessageDetailsFromDefault() {
        this.to = "mishajw@gmail.com";
        this.cc = "mishajw@gmail.com";
        this.subject = "hello";
        this.body = "world";
        this.attachmentPath = "/home/misha/Pictures/Wallpapers/rollingplanes.png";
    }

    /**
     * Send the message containing user input
     */
    private void sendMessage() {
        try {
            Message message = this.getMessage();

            if (message == null) {
                return;
            }

            __logger.info("Sending message...");
            Transport.send(message);

            System.out.println("Message sent.");
        } catch (MessagingException e) {
            __logger.error("Couldn't send message", e);
        }
    }

    /**
     * Gets a new message based on fields
     * @return the message
     */
    private Message getMessage() {
        __logger.info("Setting message");

        try {
            Message message = new MimeMessage(mail.getSession());

            __logger.info("Setting message basic details");
            message.setFrom(new InternetAddress(mail.getUsername()));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));

            message.setSubject(this.subject);

            MimeMultipart messageBody = this.getMessageBody();

            __logger.info("Adding message body");
            message.setContent(messageBody);

            return message;
        } catch (MessagingException e) {
            __logger.error("Couldn't make message", e);
            return null;
        }
    }

    /**
     * Get the main message body from fields
     * @return the message body
     */
    private MimeMultipart getMessageBody() {
        __logger.info("Setting message body");

        MimeMultipart messageBody = new MimeMultipart();

        __logger.info("Setting message text");
        MimeBodyPart text = new MimeBodyPart();
        try {
            text.setText(this.body);
        } catch (MessagingException e) {
            __logger.error("Couldn't add body to message", e);
        }

        try {
            messageBody.addBodyPart(text);
        } catch (MessagingException e) {
            __logger.error("Couldn't add main text to message", e);
        }

        if (!attachmentPath.replace(" ", "").equals("")) {
            __logger.info("Setting attachment");
            MimeBodyPart attachment = new MimeBodyPart();
            try {
                File file = new File(this.attachmentPath);

                if (!file.exists()) {
                    System.out.println("File doesn't exist: " + this.attachmentPath);
                    return messageBody;
                }

                attachment.attachFile(file);
                attachment.setHeader("Content-Type", "text/plain; charset=\"us-ascii\"; name=\"mail.txt\"");
            } catch (IOException | MessagingException e) {
                __logger.error("Couldn't attach file", e);
            }
//            DataSource dataSource = new FileDataSource(this.attachmentPath);
//            String[] pathSplit = this.attachmentPath.split("/");
//            String fileName = pathSplit[pathSplit.length - 1];
//
//            try {
//                attachment.setDataHandler(new DataHandler(dataSource));
//                attachment.setFileName(fileName);
//            } catch (MessagingException e) {
//                __logger.error("Couldn't attach file " + this.attachmentPath, e);
//            }


            try {
                messageBody.addBodyPart(attachment);
            } catch (MessagingException e) {
                __logger.error("Couldn't add attachment to message", e);
            }
        }

        __logger.info("Done setting body");

        return messageBody;
    }
}
