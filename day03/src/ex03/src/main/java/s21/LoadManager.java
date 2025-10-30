package s21;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class LoadManager {
    private final Queue<URL> buffer = new LinkedList<>();
    private final int bufferSize = 10;
    private int threadsCount = 3;
    private ArrayList<Thread> threads = new ArrayList<>();
    private String downloadDir = "";
    private String sourceList;

    LoadManager(int threadsCount, String sourceList, String downloadDir) {
        this.threadsCount = threadsCount;
        this.downloadDir = downloadDir;
        this.sourceList = sourceList;
    }

    class Producer extends Thread {
        Producer(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.printf("> %s START\n", getName());
            try (FileReader reader = new FileReader(sourceList); BufferedReader br = new BufferedReader(reader)) {
                while (br.ready()) {
                    synchronized (buffer) {
                        while (buffer.size() == bufferSize) {
                            buffer.wait();
                        }
                        String line = br.readLine().trim();
                        buffer.add(new URL(line));
                        System.out.println("Add to buffer: " + line);
                        buffer.notifyAll();
                    }
                }
            } catch (Exception e) {
                System.out.printf("Exception (%s): %s\n", getName(), e.getMessage());
                e.printStackTrace();
            }
            System.out.printf("> %s STOP\n", getName());
        }
    }

    class Worker extends Thread {
        Worker(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.printf("> %s START\n", getName());
            while (!interrupted()) {
                try {
                    URL url = null;
                    synchronized (buffer) {
                        try {
                            while (buffer.isEmpty()) {
                                buffer.wait();
                            }
                            url = buffer.poll();
                            buffer.notifyAll();
                        } catch (InterruptedException e) {
                            break;
                        }
                    }

                    String fileName = new File(url.getPath()).getName();
                    Path downloadPath = Paths.get(downloadDir, fileName);

                    System.out.printf("%s start download file %s\n", getName(), fileName);
                    InputStream in = url.openStream();
                    Files.copy(in, downloadPath, StandardCopyOption.REPLACE_EXISTING);
                    in.close();
                    System.out.printf("%s finish download file %s\n", getName(), fileName);
                } catch (Exception e) {
                    System.out.printf("Exception (%s): %s\n", getName(), e.getMessage());
                    e.printStackTrace();
                }
            }
            System.out.printf("> %s STOP\n", getName());
        }
    }

    public void startDownloading() {
        Producer producer = new Producer("Producer");
        producer.start();

        try {
            File downloadsDir = new File(downloadDir);
            if (!downloadsDir.exists()) {
                downloadsDir.mkdir();
            }
        } catch (Exception e) {
            System.out.println("Exception startDownloading(): " + e.getMessage());
            e.printStackTrace();
            return;
        }

        for (int i = 0; i < threadsCount; i++) {
            Thread t = new Worker("Thread-" + (i + 1));
            t.start();
            threads.add(t);
        }

        while (true) {
            if (!producer.isAlive() && buffer.isEmpty()) {
                System.out.println("Download is done");
                for (Thread t : threads) {
                    t.interrupt();
                    System.out.printf("%s try stop\n", t.getName());
                }
                return;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("Exception startDownloading() - end: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
