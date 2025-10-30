package s21;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

public class App {
    static int COUNT = 5;

    static class MyThread extends Thread {
        private final String msg;
        private static final Object lock = new Object();

        MyThread(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            synchronized (lock) {
                try {
                    for (int i = 0; i < COUNT; i++) {
                        System.out.println(msg);
                        lock.notifyAll();
                        if (i == COUNT - 1) {
                            break;
                        }
                        lock.wait();
                    }
                } catch (Exception ignore) {
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        setupArgs(args);

        new MyThread("Egg").start();
        new MyThread("Hen").start();
    }

    static void setupArgs(String[] args) {
        Options options = new Options();
        options.addOption("c", "count", true, "Count of messages");
        DefaultParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("c")) {
                COUNT = Integer.parseInt(cmd.getOptionValue("c"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }
}
