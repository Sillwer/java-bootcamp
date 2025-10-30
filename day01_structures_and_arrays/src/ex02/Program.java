package ex02;

import java.util.Scanner;

public class Program {
    private static final int STOP_WORD = 42;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int countPrimeNumbers = 0;

        while (scanner.hasNextInt()) {
            int num = scanner.nextInt();
            if (num == STOP_WORD) {
                break;
            }

            int sumOfDigits = intSumOfDigits(num);

            if (intIsPrime(sumOfDigits)) {
                countPrimeNumbers++;
            }
        }

        System.out.printf("Count of coffee-request – %d\n", countPrimeNumbers);
        scanner.close();
    }

    private static int intSumOfDigits(int num) {
        int sum = 0;

        while (num > 0) {
            sum += num % 10;
            num /= 10;
        }

        return sum;
    }

    private static boolean intIsPrime(int num) {
        boolean isPrime = true;

        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                isPrime = false;
                break;
            }
        }

        return isPrime;
    }
}
