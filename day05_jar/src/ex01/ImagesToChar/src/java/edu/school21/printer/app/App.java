package edu.school21.printer.app;

import edu.school21.printer.logic.Bmp;
import edu.school21.printer.logic.ConsolePrinter;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    static final String usageMsg = "Usage: --img=resources/it.bmp [--black=O] [--white=.]";
    static char black = '#';
    static char white = ' ';
    static String imgPath = null;
    static InputStream inputStream = null;

    public static void main(String[] args) {
        setupArgs(args);
        ConsolePrinter printer = new ConsolePrinter(new Bmp());

        try {
            if (imgPath == null) {
                System.out.println("Load standard image it.bmp from jar resources");
                ClassLoader classloader = App.class.getClassLoader();
                InputStream stream = classloader.getResourceAsStream("resources/it.bmp");
                if (stream != null) {
                    inputStream = stream;
                } else {
                    System.out.println("resource NOT found");
                    System.exit(-1);
                }
            } else {
                inputStream = Files.newInputStream(Paths.get(imgPath));
            }

            printer.load(inputStream);
            printer.printInfo();
            printer.setPixelChar("" + black + white);
            printer.print();
            inputStream.close();
        } catch (Exception e) {
            System.out.println("\nException: " + e.getMessage());
            System.exit(-1);
        }
    }

    static private void setupArgs(String[] args) {
        Character b = null, w = null;
        try {
            for (String arg : args) {
                if (arg.compareTo("-h") == 0 || arg.compareTo("--help") == 0) {
                    System.out.println(usageMsg);
                    System.exit(0);
                } else if (arg.startsWith("--white=")) {
                    String a = arg.split("=")[1];
                    if (a.length() != 1) {
                        throw new Exception();
                    }
                    w = a.charAt(0);
                } else if (arg.startsWith("--black=")) {
                    String a = arg.split("=")[1];
                    if (a.length() != 1) {
                        throw new Exception();
                    }
                    b = a.charAt(0);
                } else if (arg.startsWith("--img=")) {
                    imgPath = arg.split("=")[1];
                }
            }

            if (b != null && w != null) {
                black = b;
                white = w;
            } else {
                System.out.printf("setup standard chars: black '%c', white '%c'\n", black, white);
            }
        } catch (Exception e) {
            System.out.println(usageMsg);
            System.exit(-1);
        }
    }
}