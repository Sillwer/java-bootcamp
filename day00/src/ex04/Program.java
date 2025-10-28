package ex04;

import java.util.Scanner;

public class Program {
    private static final int UNICODE_CHARS_COUNT = 65535;
    private static final int SIZE_OF_TOP = 10;
    private static final int SCALE_MAX_HEIGHT = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            System.out.println("Illegal Input");
            scanner.close();
            System.exit(-1);
        }

        String input = scanner.nextLine();
        scanner.close();
        if (input.length() > 999) {
            System.out.println("Illegal Input: length more than 999");
            System.exit(-1);
        }

        // frequency[код символа] = количество вхождений
        int[] frequency = countFrequency(input);

        int[] topTenCharCode = findTop(frequency);

        printStatistic(frequency, topTenCharCode);
    }

    private static int[] countFrequency(String input) {
        int[] frequency = new int[UNICODE_CHARS_COUNT];

        for (char ch : input.toCharArray()) {
            frequency[ch]++;
        }

        return frequency;
    }

    private static int[] findTop(int[] frequency) {
        int[] topCharCode = new int[SIZE_OF_TOP];
        for (int i = 0; i < SIZE_OF_TOP; i++) {
            topCharCode[i] = '\n'; // символ, частота которого точно равна 0 (опираясь на условия задачи)
        }

        for (int code = 0; code < UNICODE_CHARS_COUNT; code++) {
            if (frequency[code] == 0) {
                continue;
            }

            for (int i = 0; i < SIZE_OF_TOP; i++) {
                if (frequency[code] > frequency[topCharCode[i]] ||
                        (frequency[code] == frequency[topCharCode[i]] && code < topCharCode[i])) {
                    putItemToPosition(topCharCode, code, i);
                    break;
                }
            }
        }

        return topCharCode;
    }

    private static void putItemToPosition(int[] topTenCharCode, int item, int position) {
        for (int i = topTenCharCode.length - 1; i > position; i--) {
            topTenCharCode[i] = topTenCharCode[i - 1];
        }
        topTenCharCode[position] = item;
    }

    private static void printStatistic(int[] frequency, int[] topCharCode) {
        double divisionPrice = ((double) frequency[topCharCode[0]]) / SCALE_MAX_HEIGHT;
        int[] scale = new int[SIZE_OF_TOP];
        for (int i = 0; i < SIZE_OF_TOP; i++) {
            scale[i] = (int) (frequency[topCharCode[i]] / divisionPrice);
        }

        System.out.println();
        for (int row = SCALE_MAX_HEIGHT; row >= 0; row--) {
            for (int col = 0; col < SIZE_OF_TOP; col++) {
                if (row > scale[col]) {
                    System.out.println();
                    break;
                } else if (row == scale[col]) {
                    System.out.printf("%3d", frequency[topCharCode[col]]);
                } else {
                    System.out.printf("%3c", '#');
                }
            }
        }
        System.out.println();

        for (int i = 0; i < SIZE_OF_TOP; i++) {
            System.out.printf("%3c", topCharCode[i]);
        }
        System.out.println();
    }
}
