package com.example.marvelmovies;

import java.util.ArrayList;
import java.util.List;

public class Validation {
    public static List<String> validInput(String title, String year, String sales) {
        List<String> errorMsg = new ArrayList<>();
        if (title.isEmpty()) {
            errorMsg.add("Title cannot be empty");
        }
        else if (!title.matches("[A-Z][\\W\\w\\s]*")) {
            errorMsg.add("Title needs to begin with capital letter");
        }
        if (year.isEmpty()) {
            errorMsg.add("Year cannot be empty");
        }
        else if (!year.matches("\\d\\d\\d\\d")) {
            errorMsg.add("Year must contain four digits");
        }
        if (sales.isEmpty()) {
            errorMsg.add("Sales cannot be empty");
        }
        else if (!sales.matches("(\\d+)?\\.(\\d+)?")) {
            errorMsg.add("Sales can only contain digits. The decimal point is optional. " +
                    "If the decimal point is included, then there must be at least one before " +
                    "and one number after it");
        }
        return errorMsg;
    }
}
