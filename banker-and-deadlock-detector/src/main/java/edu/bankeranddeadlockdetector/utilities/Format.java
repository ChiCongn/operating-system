package edu.bankeranddeadlockdetector.utilities;

import java.util.Arrays;

public class Format {
    public static String formatArray(int[] array) {
        if (array == null) return "";
        StringBuilder toString = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            toString.append(array[i]);
            if (i < array.length - 1) {  // Add comma unless it's the last element
                toString.append(" ");
            }
        }
        return toString.toString();
    }

    public static int[] parseArray(String input) {
        System.out.println("parse array");
        if (input == null || input.trim().isEmpty()) {
            return new int[0];
        }
        try {
            String[] parts = input.trim().split("\\s+"); // Split by spaces only
            return Arrays.stream(parts)
                    .mapToInt(Integer::parseInt)
                    .toArray();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please enter numbers separated by spaces.");
            return new int[0];
        }
    }


}

