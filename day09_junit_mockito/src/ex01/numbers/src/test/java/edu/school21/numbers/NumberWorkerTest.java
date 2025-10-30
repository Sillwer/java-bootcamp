package edu.school21.numbers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class NumberWorkerTest {
    static NumberWorker w = new NumberWorker();

    @ParameterizedTest(name = "is prime: {0}")
    @ValueSource(ints = {3, 5, 37, 41, 283, 293, 3877, 9433})
    void isPrimeForPrimes(int number) {
        assertTrue(w.isPrime(number));
    }

    @ParameterizedTest(name = "is NOT prime {0}")
    @ValueSource(ints = {4, 38, 42, 284, 3878, 6969})
    void isPrimeForNotPrimes(int number) {
        assertFalse(w.isPrime(number));
    }

    @ParameterizedTest(name = "incorrect number: {0}")
    @ValueSource(ints = {-69, -4, -3, 0, 1})
    void isPrimeForIncorrectNumbers(int number) {
        assertThrowsExactly(IllegalArgumentException.class, () -> w.isPrime(number));
    }

    @ParameterizedTest(name = "sum of digits {0} is {1}")
    @CsvFileSource(resources = "/data.csv", ignoreLeadingAndTrailingWhitespace = true)
    void digitsSumFromCsv(int number, int sum) {
        assertEquals(sum, w.digitsSum(number));
    }
}
