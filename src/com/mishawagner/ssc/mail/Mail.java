package com.mishawagner.ssc.mail;

import com.sun.mail.imap.IMAPFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by misha on 04/11/15.
 *
 * Holds all mail operations
 */
public class Mail {
    private static final Logger __logger = LoggerFactory.getLogger(Mail.class);

    /**
     * Mail session
     */
    private Session session;

    /**
     * Credentials
     */
    private String username, password, smtpHost, imapHost;

    /**
     * Messages in the folder
     */
    private Message[] messages;

    /**
     * Start connecting to mail server
     */
    public Mail() {
        this.login();
        this.setMessages();
    }

    /**
     * Login to the GMail SMTP server
     * Sets the session variable
     */
    private void login() {
        setCredentials();
        setSysProperties();

        Properties sysProperties = System.getProperties();
        sysProperties.put("mail.store.protocol", "imaps");
        this.session = Session.getDefaultInstance(sysProperties);
    }

    /**
     * Get the credentials from a property file
     */
    private void setCredentials() {
        Properties credentials = new Properties();

        try {
            credentials.load(new FileInputStream("res/credentials.properties"));
        } catch (IOException e) {
            __logger.error("Couldn't find credentials.properties, not setting credentials", e);
            return;
        }

        this.username = credentials.getProperty("username");
        this.password = credentials.getProperty("password");
        this.smtpHost = credentials.getProperty("smtphost");
        this.imapHost = credentials.getProperty("imaphost");

        if (password == null || password.equals("")) {
            password = this.getPasswordFromUser();
        }

        __logger.info("Set credentials");
    }

    /**
     * Set the system properties up for sending/recieving mail
     */
    private void setSysProperties() {
        Properties sysProperties = System.getProperties();

        // SMTP properties
        sysProperties.put("mail.smtp.auth", "true");
        sysProperties.put("mail.smtp.starttls.enable", "true");
        sysProperties.put("mail.smtp.host", smtpHost);
        sysProperties.put("mail.smtp.port", "587");

        //IMAP properties
        sysProperties.put("mail.store.protocol", "imaps");

        // Credentials
        sysProperties.put("mail.user", username);
        sysProperties.put("mail.password", password);

        __logger.info("Set system properties");
    }

    /**
     * Set the latest messages from the server
     */
    public void setMessages() {
        try {
            __logger.info("Getting store...");
            Store store = session.getStore("imaps");
            store.connect(this.imapHost, this.username, this.password);

            __logger.info("Getting inbox...");
            IMAPFolder folder = (IMAPFolder) store.getFolder("inbox");

            if (!folder.isOpen()) {
                folder.open(Folder.READ_WRITE);
            }

            this.messages = folder.getMessages();

            __logger.info("Set messages");
        } catch (MessagingException e) {
            __logger.error("Couldn't get messages", e);
        }
    }

    /**
     * Get the password from the user
     * @return
     */
    private String getPasswordFromUser() {
        JPasswordField passField = new JPasswordField(10);
        int action = JOptionPane.showConfirmDialog(null, passField, "Please enter your password:", JOptionPane.OK_CANCEL_OPTION);

        if (action < 0) {
            System.exit(0);
        }

        __logger.info("Got password from user");

        return new String(passField.getPassword());
    }

    public Message[] getMessages() {
        return messages;
    }
}
