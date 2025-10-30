package ex01;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TextProcessor {
    static final Set<String> wordsLibrary = new HashSet<>();
    final private Map<String, Integer> wordFrequency = new HashMap<>();

    void appendFromFile(String fileName) throws FileNotFoundException, SecurityException, IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                for (String word : line.split("\\s")) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    wordsLibrary.add(word);

                    if (!wordFrequency.containsKey(word)) {
                        wordFrequency.put(word, 1);
                    } else {
                        wordFrequency.compute(word, (w, counter) -> counter != null ? counter + 1 : null);
                    }
                }
            }
        } catch (FileNotFoundException | SecurityException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Returns value from
     *
     * @return -1 if other text is null
     */
    double calculateSimilarity(TextProcessor other) {
        if (other == null) {
            return -1;
        } else if (this == other) {
            return 1;
        }

        double numerator = 0;
        double denominatorA = 0;
        double denominatorB = 0;
        for (String word : wordsLibrary) {
            Integer a = this.wordFrequency.get(word);
            if (a == null) {
                a = 0;
            }
            Integer b = other.wordFrequency.get(word);
            if (b == null) {
                b = 0;
            }

            numerator += a * b;
            denominatorA += a * a;
            denominatorB += b * b;
        }
        double denominator = Math.sqrt(denominatorA) * Math.sqrt(denominatorB);

        return numerator / denominator;
    }

    static void printWordsLibrary(OutputStream outStream) {
        wordsLibrary.forEach((str) -> {
            try {
                outStream.write(String.format(str + " ").getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    void printFrequency() {
        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            System.out.printf("%s: %d | ", entry.getKey(), entry.getValue());
        }
        System.out.println();
    }
}
