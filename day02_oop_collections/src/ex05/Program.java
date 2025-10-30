package ex05;

import java.util.Scanner;
import java.util.UUID;

public class Program {
    public static void main(String[] args) {

        boolean devMode = false;
        for (String arg : args) {
            if (arg.equals("--profile=dev")) {
                devMode = true;
                break;
            }
        }

        Scanner scanner = new Scanner(System.in);
        Menu menu = new Menu(devMode, scanner, new TransactionsService());

        boolean run = true;
        while (run) {
            menu.printMenu();
            int item = menu.getInputMenuItem();
            run = menu.perform(item);
        }

        scanner.close();
    }
}
