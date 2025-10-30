package edu.school21.numbers;

public class NumberWorker {
    public boolean isPrime(int number) {
        if (number <= 1) {
            throw new IllegalArgumentException("The input is expected to be more than 1");
        }

        long x = Math.round(Math.sqrt(number));
        for (int divider = 2; divider <= x; divider++) {
            if (number % divider == 0) {
                return false;
            }
        }

        return true;
    }

    public int digitsSum(int number) {
        if (number < 0) {
            number *= -1;
        }

        int sum = 0;
        while (number > 0) {
            sum += number % 10;
            number /= 10;
        }

        return sum;
    }
}