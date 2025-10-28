package ex03;

import java.util.Scanner;

public class Program {
    static final String STOP_WORD = "42";

    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);
        int weekN = 0;
        long gradeChain = 0;
        long chainDigitPosition = 1;

        while (inputScanner.hasNext()) {
            String weekLine = inputScanner.nextLine();
            if (weekLine.equals(STOP_WORD)) {
                break;
            }

            weekN++;
            if (!weekLine.equalsIgnoreCase("Week " + weekN)) {
                System.err.println("Illegal Argument");
                inputScanner.close();
                System.exit(-1);
            }
            if (weekN > 18) {
                System.out.println("The maximum number of weeks for analysis is 18.");
                weekN -= 1; // = 18
                break;
            }

            String gradesLine = inputScanner.nextLine();
            Scanner gradeScanner = new Scanner(gradesLine);
            if (!gradeScanner.hasNextInt()) {
                System.err.println("Illegal Argument");
                inputScanner.close();
                System.exit(-1);
            }
            int minGrade = intGetMin(gradeScanner);
            gradeScanner.close();

            gradeChain += minGrade * chainDigitPosition;
            chainDigitPosition *= 10;
        }
        inputScanner.close();

        for (int n = 1; n <= weekN; n++) {
            System.out.printf("Week %d ", n);

            int grade = (int) (gradeChain % 10);
            gradeChain /= 10;
            printLine(grade, '=', '>');
            System.out.println();
        }
    }

    /**
     * Функция ожидает сканер, нацеленный минимум на 1 число
     */
    static int intGetMin(Scanner sc) {
        int min = sc.nextInt(), i = 0;
        while (i++ < 5 && sc.hasNextInt()) {
            int num = sc.nextInt();
            if (num < min) {
                min = num;
            }
        }
        return min;
    }

    static void printLine(int len, char lineChar, char endChar) {
        for (int i = 0; i < len; i++) {
            System.out.print(lineChar);
        }
        System.out.print(endChar);
    }
}