package com.siteminder.util;

import java.util.regex.Pattern;

public class EmailUtil {
    private static final String REGEX_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public static boolean patternMatches(String emailAddress) {
        return Pattern.compile(REGEX_PATTERN)
                .matcher(emailAddress)
                .matches();
    }
}
