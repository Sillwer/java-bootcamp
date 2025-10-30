package s21;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

public class App {
    static int STEP_COUNT = 10;

    static class MyThread extends Thread {
        String msg;

        MyThread(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            for (int i = 0; i < STEP_COUNT; i++) {
                System.out.println(msg);
            }
        }
    }

    static class MyRunnable implements Runnable {
        String msg;

        MyRunnable(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            for (int i = 0; i < STEP_COUNT; i++) {
                System.out.println(msg);
            }
        }
    }

    public static void main(String[] args) {
        setupArgs(args);

        MyThread t1 = new MyThread("Egg");
        Thread t2 = new Thread(new MyRunnable("Hen"));

        t1.start();
        t2.start();

        for (int i = 0; i < STEP_COUNT; i++) {
            System.out.println("Human");
        }
    }

    static void setupArgs(String[] args) {
        Options options = new Options();
        options.addOption("c", "count", true, "Count of messages per process");
        DefaultParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("c")) {
                STEP_COUNT = Integer.parseInt(cmd.getOptionValue("c"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }
}
