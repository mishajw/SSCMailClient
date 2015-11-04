package com.mishawagner.ssc.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Session;
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

    private Session session;

    public Mail() {
        this.login();

    }

    /**
     * Login to the GMail SMTP server
     * Sets the session variable
     */
    private void login() {
        Properties credentials = new Properties();
        try {
            credentials.load(new FileInputStream("res/credentials.properties"));
        } catch (IOException e) {
            __logger.error("Couldn't find credentials.properties", e);
            return;
        }


        String username = credentials.getProperty("username");
        String password = credentials.getProperty("password");
        String smtpHost = credentials.getProperty("smtphost");

        Properties sysProperties = System.getProperties();
        sysProperties.put("mail.smtp.auth", "true");
        sysProperties.put("mail.smtp.starttls.enable", "true");
        sysProperties.put("mail.smtp.host", smtpHost);
        sysProperties.put("mail.smtp.port", "587");

        if (password == null || password.equals("")) {
            password = this.getPasswordFromUser();
        }

        this.session = Session.getDefaultInstance(sysProperties);
    }

    private String getPasswordFromUser() {
        JPasswordField passField = new JPasswordField(10);
        int action = JOptionPane.showConfirmDialog(null, passField, "Please enter your password:", JOptionPane.OK_CANCEL_OPTION);

        if (action < 0) {
            System.exit(0);
        }

        return new String(passField.getPassword());
    }
}
