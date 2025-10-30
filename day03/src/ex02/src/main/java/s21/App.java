package s21;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.util.Arrays;
import java.util.Random;

public class App {
    static int ARRAY_SIZE;
    static int THREADS_COUNT;

    static class SummatorThread extends Thread {
        private static Long sum = 0L;
        private final int[] arr;
        private final int startIndex;
        private final int endIndex;

        SummatorThread(int[] arr, int startIndex, int endIndex, String name) {
            this.arr = arr;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.setName(name);
        }

        @Override
        public void run() {
            int localSum = 0;
            synchronized (sum) {
                for (int i = startIndex; i < endIndex; i++) {
                    localSum += arr[i];
                }
            }
            System.out.printf("%s: from %d to %d sum is %d\n",
                    getName(), startIndex, endIndex - 1, localSum);
            sum += localSum;
        }

        static Long getSum() {
            return sum;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        parseArgs(args);

        Random random = new Random();
        int[] arr = new int[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            arr[i] = random.nextInt(1000);
            if (random.nextBoolean()) {
                arr[i] *= -1;
            }
        }

        System.out.printf("sum: %d\n", Arrays.stream(arr).sum());

        SummatorThread[] threads = new SummatorThread[THREADS_COUNT];
        int step = ARRAY_SIZE / THREADS_COUNT;

        int i = 0, j = 0;
        for (; i < THREADS_COUNT; i++, j++) {
            int indexStart = j * step;
            int indexEnd = (i < THREADS_COUNT - 1) ? indexStart + step : ARRAY_SIZE;
            threads[i] = new SummatorThread(arr, indexStart, indexEnd, "Thread " + (i + 1));
            threads[i].start();
        }

        for (SummatorThread t : threads) {
            t.join();
        }

        System.out.printf("Sum by threads: %d\n", SummatorThread.getSum());
    }

    private static void parseArgs(String[] args) {
        Options options = new Options();
        options.addOption("a", "arraySize", true, "Length of numbers array");
        options.addOption("t", "threadsCount", true, "Count of threads");

        DefaultParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            ARRAY_SIZE = Integer.parseInt(cmd.getOptionValue("a", "13"));
            THREADS_COUNT = Integer.parseInt(cmd.getOptionValue("t", "3"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        if (THREADS_COUNT > ARRAY_SIZE) {
            THREADS_COUNT = ARRAY_SIZE;
        }
    }
}
