package edu.tool.utils;

public class InputHandler {

    public static int[] convertStringToIntegerArray(String input) {
        // remove any leading or trailing spaces
        input = input.trim();

        // if the input is empty, return an empty array
        if (input.isEmpty()) {
            return new int[0];
        }

        // split by spaces
        String[] stringNumbers = input.split("\\s+");

        // convert the string to an integer array
        int[] intArray = new int[stringNumbers.length];
        for (int i = 0; i < stringNumbers.length; i++) {
            try {
                intArray[i] = Integer.parseInt(stringNumbers[i].trim()); // Parse each string to an integer
            } catch (NumberFormatException e) {
                Alert.showNotification("Input Error",
                        "Invalid number format in the input string. Please enter valid integers.");
                throw new IllegalArgumentException("Invalid number format in input string", e);
            }
        }

        return intArray;
    }

    public static double[] convertStringToDoubleArray(String input) {
        // Remove any leading or trailing spaces
        input = input.trim();

        // If the input is empty, return an empty array
        if (input.isEmpty()) {
            return new double[0];
        }

        // Split the string by spaces
        String[] stringNumbers = input.split("\\s+");

        // Convert the string array to a double array
        double[] doubleArray = new double[stringNumbers.length];
        for (int i = 0; i < stringNumbers.length; i++) {
            try {
                doubleArray[i] = Double.parseDouble(stringNumbers[i].trim()); // Parse each string to a double
            } catch (NumberFormatException e) {
                Alert.showNotification("Input Error",
                        "Invalid number format in the input string. Please enter valid decimal numbers.");
                throw new IllegalArgumentException("Invalid number format in input string", e);
            }
        }

        return doubleArray;
    }

    public static String[] convertStringToStringArray(String input) {
        // Remove any leading or trailing spaces
        input = input.trim();

        // If the input is empty, return an empty array
        if (input.isEmpty()) {
            return new String[0];
        }

        // Split the string by spaces
        return input.split("\\s+");
    }

    public static int convertStringToInteger(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            Alert.showNotification("Input Error", "Invalid integer input: " + input);
            return -1;
        }
    }


}
