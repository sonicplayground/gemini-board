package com.sonicplayground.geminiboard.common.util;

import java.time.LocalDate;

public class DateTImeUtil {

    public static LocalDate validateAndGetDate(LocalDate userInput) {
        if (userInput == null) {
            return LocalDate.now();
        }

        if (userInput.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid date");
        }

        return userInput;
    }
}
