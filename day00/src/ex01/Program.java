package ex01;

import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            System.err.println("Illegal Argument");
            scanner.close();
            System.exit(1);
        }

        int num = scanner.nextInt();
        if (num <= 1) {
            System.err.println("Illegal Argument");
            scanner.close();
            System.exit(-1);
        }

        boolean isPrime = true;
        int i;

        for (i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                isPrime = false;
                break;
            }
        }

        System.out.printf("%s %d\n", isPrime, i - 1);
        scanner.close();
    }
}
