package com.mishawagner.ssc.mail;

import java.util.Scanner;

/**
 * Created by misha on 04/11/15.
 *
 * Interacts with mail client
 */
public abstract class MailInteractor {
    /**
     * Mail interaction
     */
    protected Mail mail;
    /**
     * How to interact with the user
     */
    protected Scanner input;

    public MailInteractor(Mail mail, Scanner input) {
        this.mail = mail;
        this.input = input;
    }

    /**
     * Start interacting with the user and mail server
     */
    public abstract void start();
}
