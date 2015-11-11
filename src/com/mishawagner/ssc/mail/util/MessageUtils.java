package com.mishawagner.ssc.mail.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.mail.*;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by misha on 04/11/15.
 *
 * Utils for dealing with messages
 */
public class MessageUtils {
    private static final Logger __logger = LoggerFactory.getLogger(MessageUtils.class);

    /**
     * Get the whole body of the email
     * @param m email in question
     * @return string containing body
     */
    public static String getAllBody(Message m) {
        Multipart content;
        String allBody = "";

        try {
            content = (Multipart) m.getContent();

            for (int i = 0; i < content.getCount(); i++) {
                BodyPart part = content.getBodyPart(i);
                String disposition = part.getDisposition();

                if (disposition != null && disposition.equalsIgnoreCase("attachment")) {
                    DataHandler handler = part.getDataHandler();
                    allBody += "Message has some attachment: " + handler.getName() + "\n";
                } else {
                    allBody += "Body: " + part.getContent().toString() + "\n";
                }
            }

        } catch (IOException | MessagingException e) {
            __logger.info("Couldn't read message", e);
        }

        return allBody;
    }

    private MessageUtils() {}

    /**
     * Prtin a brief summary of the email
     * @param m email containing details
     */
    public static void printSimpleDetails(Message m) {
        try {
            System.out.println( m.getSubject());
            System.out.println("\tFROM: " + Arrays.toString(m.getFrom()));
            System.out.println("\tSEEN: " + m.isSet(Flags.Flag.SEEN) + "    RECENT: " + m.isSet(Flags.Flag.RECENT) +  "    FLAGGED: " + m.isSet(Flags.Flag.FLAGGED));
            System.out.println();
        } catch (MessagingException e) {
            __logger.error("Couldn't view message details", e);
        }
    }

    /**
     * Only get the main text body from the email
     * @param m the email in question
     * @return string containing the body
     */
    public static String getMainBody(Message m) {
        Multipart content;

        try {
            Object contentRaw = m.getContent();

            if (contentRaw instanceof Multipart) {
                content = (Multipart) contentRaw;

                for (int i = 0; i < content.getCount(); i++) {
                    BodyPart part = content.getBodyPart(i);
                    String disposition = part.getDisposition();

                    if (disposition == null || !disposition.equalsIgnoreCase("attachment")) {
                        return part.getContent().toString();
                    }
                }
            } else if (contentRaw instanceof String) {
                return (String) contentRaw;
            }
        } catch (IOException | MessagingException e) {
            __logger.info("Couldn't read message", e);
        }

        return "";
    }
}
