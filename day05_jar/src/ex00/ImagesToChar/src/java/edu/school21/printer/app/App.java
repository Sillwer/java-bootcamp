package edu.school21.printer.app;

import edu.school21.printer.logic.Bmp;
import edu.school21.printer.logic.ConsolePrinter;

public class App {
    static char black = '#';
    static char white = ' ';
    static String imgPath = null;

    public static void main(String[] args) {
        setupArgs(args);

        ConsolePrinter printer = new ConsolePrinter(new Bmp());

        try {
            printer.load(imgPath);
            printer.printInfo();
            printer.setPixelChar("" + black + white);
            printer.print();
        } catch (Exception e) {
            System.out.println("\nException: " + e.getMessage());
            System.exit(-1);
        }
    }

    static private void setupArgs(String[] args) {
        Character b = null, w = null;
        String path = null;
        try {
            for (String arg : args) {
                if (arg.startsWith("--white=")) {
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
                    path = arg.split("=")[1];
                }
            }
            if (path == null) {
                throw new Exception();
            }

            if (b != null && w != null) {
                black = b;
                white = w;
            } else {
                System.out.printf("setup standard chars: black '%c', white '%c'\n", black, white);
            }
            imgPath = path;
        } catch (Exception e) {
            System.out.println("Usage: --img=resources/it.bmp [--black=O] [--white=.]");
            System.exit(-1);
        }
    }
}