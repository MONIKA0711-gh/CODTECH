import java.io.*;
import java.util.*;

public class FileHandlingUtility {

    private static final String FILE_NAME = "example.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("FILE HANDLING UTILITY");
        System.out.println("1. Write to file");
        System.out.println("2. Read from file");
        System.out.println("3. Modify file content");
        System.out.print("Choose an operation (1/2/3): ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                writeToFile(scanner);
                break;
            case 2:
                readFromFile();
                break;
            case 3:
                modifyFile(scanner);
                break;
            default:
                System.out.println("Invalid option.");
        }

        scanner.close();
    }

    /**
     * Write content to a file (overwrites existing content).
     */
    public static void writeToFile(Scanner scanner) {
        System.out.println("Enter text to write to the file:");
        String content = scanner.nextLine();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write(content);
            System.out.println("Content written to file successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Read and display content from the file.
     */
    public static void readFromFile() {
        System.out.println("Reading from file...");

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found. Please write to the file first.");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Modify specific content in the file by replacing a word or line.
     */
    public static void modifyFile(Scanner scanner) {
        System.out.print("Enter text to find: ");
        String target = scanner.nextLine();
        System.out.print("Enter replacement text: ");
        String replacement = scanner.nextLine();

        File file = new File(FILE_NAME);
        StringBuilder content = new StringBuilder();

        // Read and modify content
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line.replace(target, replacement)).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error reading file for modification: " + e.getMessage());
            return;
        }

        // Write modified content back
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content.toString());
            System.out.println("File content modified successfully.");
        } catch (IOException e) {
            System.err.println("Error writing modified content: " + e.getMessage());
        }
    }
}
