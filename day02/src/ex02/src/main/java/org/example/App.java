package org.example;

import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.Scanner;

public class App {
    static final String STOP_WORD = "exit";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String startPath = null;
        Options options = new Options().addOption("f", "current-folder", true, "Start folder");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("f")) {
                startPath = cmd.getOptionValue("f");
            } else {
                System.out.println(
                        "Usage program with argument --current-folder=C:/git/test\nWhere 'C:/git/test' - example of path.");
                System.exit(0);
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        FileManager fm = null;
        try {
            fm = new FileManager(startPath, sc);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        boolean stop = false;
        try {
            System.out.println(startPath);
            while (!stop) {
                System.out.print("-> ");
                String inputStr = sc.nextLine().trim();
                String[] input = inputStr.split("\\s+");
                if (input.length < 1) {
                    continue;
                }

                switch (input[0]) {
                    case "ls":
                        fm.ls(Arrays.copyOfRange(input, 1, input.length));
                        break;
                    case "cd":
                        fm.cd(Arrays.copyOfRange(input, 1, input.length));
                        break;
                    case "mv":
                        fm.mv(Arrays.copyOfRange(input, 1, input.length));
                        break;
                    case "pwd":
                        fm.pwd();
                        break;
                    case STOP_WORD:
                        stop = true;
                        break;
                    default:
                        System.out.printf("Unknown command '%s'\n", input[0]);
                }
            }

        } catch (Exception e) {

        }

    }
}
