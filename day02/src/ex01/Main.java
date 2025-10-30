package ex01;

import java.io.FileOutputStream;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Program awaits 2 arguments: inputA.txt inputB.txt");
            System.exit(0);
        }

        TextProcessor A = new TextProcessor(), B = new TextProcessor();
        try {
            A.appendFromFile(args[0]);
            B.appendFromFile(args[1]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream("./src/ex01/dictionary.txt")) {
            TextProcessor.printWordsLibrary(fileOutputStream);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TextProcessor.printWordsLibrary(System.out);
        System.out.println();
        System.out.printf("Similarity = %.2f\n", A.calculateSimilarity(B));
    }
}
