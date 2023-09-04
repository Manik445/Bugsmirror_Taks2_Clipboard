import java.io.*;
import java.util.*;

public class clipboard {
    private static String clipboard = "";   

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Task 2"); 
        System.out.println("Clipboard Console Tool");

        while (true) {
            System.out.println("\nSelect operation:");
            System.out.println("1. Copy");
            System.out.println("2. Cut");
            System.out.println("3. Paste");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume  a Newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // for Consuming an Invalid input
                continue;
            }

            switch (choice) {
                case 1:
                    copyText(scanner);
                    break;
                case 2:
                    cutText(scanner);
                    break;
                case 3:
                    pasteText(scanner);
                    break;
                case 4:
                    System.out.println("Exiting Clipboard Console Tool.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void copyText(Scanner scanner) {
        System.out.print("Enter file name (e.g., file.txt): ");
        String fileName = scanner.nextLine();
        System.out.print("Enter start line number: ");
        int startLine = getIntInput(scanner);
        System.out.print("Enter start index: ");
        int startIndex = getIntInput(scanner);
        System.out.print("Enter end line number: ");
        int endLine = getIntInput(scanner);
        System.out.print("Enter end index: ");
        int endIndex = getIntInput(scanner);

        if (!validateIndices(startLine, startIndex, endLine, endIndex)) {
            System.out.println("Invalid indices. Please check your input.");
            return;
        }

        try {
            String text = readTextFromFile(fileName);
            String selectedText = getSelectedText(text, startLine, startIndex, endLine, endIndex);
            clipboard = selectedText;
            System.out.println("Text copied to clipboard: " + clipboard);
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    private static void cutText(Scanner scanner) {
        System.out.print("Enter the file name (e.g., file.txt): ");
        String fileName = scanner.nextLine();
        System.out.print("Enter the start line number: ");
        int startLine = getIntInput(scanner);
        System.out.print("Enter the start index: ");
        int startIndex = getIntInput(scanner);
        System.out.print("Enter the end line number: ");
        int endLine = getIntInput(scanner);
        System.out.print("Enter the end index: ");
        int endIndex = getIntInput(scanner);

        if (!validateIndices(startLine, startIndex, endLine, endIndex)) {
            System.out.println("Invalid indices. Please check your input.");
            return;
        }

        try {
            String text = readTextFromFile(fileName);
            String selectedText = getSelectedText(text, startLine, startIndex, endLine, endIndex);
            clipboard = selectedText;
            System.out.println("Text cut to clipboard: " + clipboard);

            // Remove the cut text from the file
            String updatededText = text.replace(selectedText, "");
            writeTextToFile(fileName, updatededText);
            System.out.println("Text removed from the file.");
        } catch (IOException e) {
            System.out.println("Error reading/writing the file: " + e.getMessage());
        }
    }

    private static void pasteText(Scanner scanner) {
        if (clipboard.isEmpty()) {
            System.out.println("Clipboard is empty. Nothing to paste.");
            return;
        }

        System.out.print("Enter the file name (e.g., file.txt) to paste into: ");
        String fileName = scanner.nextLine();
        System.out.print("Enter the line number to paste at: ");
        int pasteLine = getIntInput(scanner);
        System.out.print("Enter the index to paste at: ");
        int pasteIndex = getIntInput(scanner);

        try {
            String text = readTextFromFile(fileName);

            if (pasteLine < 1 || pasteLine > text.split("\n").length + 1 || pasteIndex < 0 || pasteIndex > text.length()) {
                System.out.println("Invalid paste location. Please check your input.");
                return;
            }

            StringBuilder newText = new StringBuilder(text);
            int insertionIndex = getInsertionIndex(text, pasteLine, pasteIndex);
            newText.insert(insertionIndex, clipboard);
            writeTextToFile(fileName, newText.toString());
            System.out.println("Text pasted into the file.");
        } catch (IOException e) {
            System.out.println("Error reading/writing the file: " + e.getMessage());
        }
    }

    private static String readTextFromFile(String fileName) throws IOException {
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }
        }
        return text.toString();
    }

    private static void writeTextToFile(String fileName, String text) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(text);
        }
    }

    private static String getSelectedText(String text, int startLine, int startIndex, int endLine, int endIndex) {
        String[] lines = text.split("\n");
        StringBuilder selectedText = new StringBuilder();

        for (int i = startLine - 1; i < endLine; i++) {
            String line = lines[i];
            if (i == startLine - 1) {
                selectedText.append(line.substring(startIndex));
            } else if (i == endLine - 1) {
                selectedText.append("\n").append(line.substring(0, endIndex));
            } else {
                selectedText.append("\n").append(line);
            }
        }
        return selectedText.toString();
    }

    private static int getInsertionIndex(String text, int pasteLine, int pasteIndex) {
        String[] lines = text.split("\n");
        int index = 0;
        for (int i = 0; i < pasteLine - 1; i++) {
            index += lines[i].length() + 1; // +1 for newline character
        }
        index += pasteIndex;
        return index;
    }

    private static int getIntInput(Scanner scanner) {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine(); // Consume an invalid input
            return getIntInput(scanner); // Recursively try again and try to enter a valid input
        }
    }

    private static boolean validateIndices(int startLine, int startIndex, int endLine, int endIndex) {
        return startLine > 0 && startIndex >= 0 && endLine > 0 && endIndex >= 0 && endLine >= startLine;
    }
}
