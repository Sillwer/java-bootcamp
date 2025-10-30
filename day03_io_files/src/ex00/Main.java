package ex00;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static final String SIGNATURES_FILE = "src/ex00/signatures.txt";
    private static final String OUTPUT_FILE = "src/ex00/result.txt";
    private static final String STOP_WORD = "42";

    public static void main(String[] args) {
        System.out.println(">>> " + System.getProperty("user.dir") + " <<<");

        try {
            FileSignature.setupSignature(SIGNATURES_FILE);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        System.out.printf("Load signatures from [%s] ...\n", SIGNATURES_FILE);
        FileSignature.printSignatures();
        System.out.printf("\nInput file path (absolute or from '%s%c') | Enter '%s' to stop program\n",
                System.getProperty("user.dir"), System.getProperty("file.separator").charAt(0), STOP_WORD);

        try (Scanner scanner = new Scanner(System.in);
                FileOutputStream fileOutStream = new FileOutputStream(OUTPUT_FILE, false)) {
            while (true) {
                try {
                    System.out.print("-> ");
                    String input = scanner.nextLine().trim();
                    if (input.equals("42")) {
                        break;
                    }

                    String signatureName = FileSignature.defineSignature(input);
                    if (signatureName == null) {
                        System.out.println("UNDEFINED");
                    } else {
                        System.out.println("PROCESSED");
                        // fileOutStream.write(String.format("%-8s\t(%s)\n", signatureName,
                        // input).getBytes());
                        fileOutStream.write(String.format("%s\n", signatureName).getBytes());
                    }
                } catch (FileNotFoundException | SecurityException e) {
                    System.out.println("File reading error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            System.out.println("Input error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
