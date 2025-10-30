package edu.school.game.gamelogic;

import edu.school.game.exceptions.UserLostException;
import edu.school.game.exceptions.UserWinException;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class MainProcessor {
    private final boolean devMode;
    private final Scanner scanner;
    private final Controller controller;
    private final String RESTART_MAP = "4";
    private final String REGENERATE_MAP = "6";
    private final String CONFIRM_ENEMY_STEP = "8";
    private final String GIVE_UP = "9";
    private int PAUSE_MS = 100;

    public MainProcessor(Controller controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
        this.devMode = controller.isDevMode();
    }

    public void startEngine() {
        controller.generateMap();
        String extraMsg = null;
        controller.updateScreen();
        while (true) {
            try {
                String input = "";
                while (true) {
                    try {
                        System.out.print("Enter [WASD] to move");
                        if (extraMsg != null) {
                            System.out.print(" | " + extraMsg);
                            extraMsg = null;
                        }
                        System.out.print("\n> ");

                        input = scanner.nextLine().trim().toUpperCase();
                        if (GIVE_UP.equals(input)) {
                            System.out.println(">> The player gave up <<<");
                            return;
                        } else if (REGENERATE_MAP.equals(input)) {
                            controller.generateMap();
                            controller.updateScreen();
                            continue;
                        } else if (RESTART_MAP.equals(input)) {
                            controller.restartMap();
                            controller.updateScreen();
                            continue;
                        } else if (input.length() != 1 || !"AWSD".contains(input)) {
                            continue;
                        }

                        if (!controller.movePlayer(input.charAt(0))) {
                            extraMsg = "Can't move in that direction [" + input + "]";
                            controller.updateScreen();
                            continue;
                        }
                        break;
                    } catch (NoSuchElementException | IllegalStateException e) {
                        System.out.println(e.getMessage());
                    }
                }

                System.out.println();
                controller.updateScreen();

                int enemyIndex = 0;
                while (controller.moveEnemy(enemyIndex++)) {
                    if (devMode) {
                        input = "";
                        while (!CONFIRM_ENEMY_STEP.equals(input)) {
                            try {
                                System.out.print("Enemy tern. Enter 8 to continue\n> ");
                                input = scanner.nextLine();
                                if (GIVE_UP.equals(input)) {
                                    System.out.println(">> The player gave up <<<");
                                    return;
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    } else {
                        Thread.sleep(PAUSE_MS);
                    }

                    System.out.println();
                    controller.updateScreen();
                }
            } catch (UserLostException | UserWinException e) {
                System.out.println();
                controller.updateScreen();
                System.out.println(e.getMessage());
                return;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}