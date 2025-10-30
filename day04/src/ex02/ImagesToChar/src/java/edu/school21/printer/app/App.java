package edu.school21.printer.app;

import edu.school21.printer.logic.Bmp;
import edu.school21.printer.logic.ConsolePrinter;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.*;

public class App {
    static JArgParser jArgParser = new JArgParser();
    static private String backColor;
    static private String faceColor;
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
            printer.setPixelColors(new String[]{faceColor, backColor});
            printer.print();
            inputStream.close();
        } catch (Exception e) {
            System.out.println("\nException: " + e.getMessage());
            System.exit(-1);
        }
    }

    static private void setupArgs(String[] args) {
        JCommander.newBuilder().addObject(jArgParser).build().parse(args);

        if (jArgParser.help) {
            System.out.println("Usage: [--img=resources/it.bmp] [--white=RED] -[-black=BLUE]");
            System.out.print("COLORS: ");
            for (Bmp.COLOR color : Bmp.COLOR.values()) {
                System.out.print(color + " ");
            }
            System.out.println();
            System.exit(0);
        }

        imgPath = jArgParser.impPath;

        if (jArgParser.backColor != null && jArgParser.faceColor != null) {

            boolean backOk = false, faceOk = false;
            for (Bmp.COLOR color : Bmp.COLOR.values()) {
                if (jArgParser.backColor.compareTo(color.toString()) == 0) {
                    backOk = true;
                }
                if (jArgParser.faceColor.compareTo(color.toString()) == 0) {
                    faceOk = true;
                }
            }
            if (!backOk || !faceOk) {
                System.out.print("Unexpected color ( ");
                if (!backOk) {
                    System.out.print(jArgParser.backColor + " ");
                }
                if (!faceOk) {
                    System.out.print(jArgParser.faceColor + " ");
                }
                System.out.print(") Expected: ");
                for (Bmp.COLOR color : Bmp.COLOR.values()) {
                    System.out.print(color + " ");
                }
                System.out.println();
                System.exit(0);
            }

            backColor = jArgParser.backColor;
            faceColor = jArgParser.faceColor;
        } else {
            backColor = Bmp.COLOR.RED.toString();
            faceColor = Bmp.COLOR.GREEN.toString();
            System.out.println("setup standard colors");
        }
    }

    @Parameters(separators = "=")
    static private class JArgParser {
        @Parameter
        public List<String> parameters = new ArrayList<>();

        @Parameter(names = {"--img"})
        public String impPath;

        @Parameter(names = "--white")
        public String faceColor;

        @Parameter(names = "--black")
        public String backColor;

        @Parameter(names = {"--help", "-h"}, description = "Show usage information", help = true)
        public boolean help = false;
    }
}