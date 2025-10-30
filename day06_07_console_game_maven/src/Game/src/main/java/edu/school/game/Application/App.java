package edu.school.game.Application;

import com.beust.jcommander.JCommander;
import edu.school.game.gamelogic.Controller;
import edu.school.game.models.UnitType;
import edu.school.game.gamelogic.MainProcessor;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.beust.jcommander.Parameters;
import com.beust.jcommander.Parameter;

public class App {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            AppArgs appArgs = parseCmdArgs(args);
            setupUnitsParams(appArgs.getProfile());

            boolean devMode = appArgs.getProfile().equalsIgnoreCase("dev");
            if (!devMode) {
                clearScreen();
            }
            Controller controller = new Controller(
                    appArgs.getSize(),
                    appArgs.getEnemiesCount(),
                    appArgs.getWallsCount(),
                    devMode
            );

            MainProcessor mainProcessor = new MainProcessor(controller, scanner);
            mainProcessor.startEngine();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    static private AppArgs parseCmdArgs(String[] args) {
        AppArgs appArgs = new AppArgs();
        JCommander jCommander = null;
        try {
            jCommander = JCommander.newBuilder().addObject(appArgs).build();
            jCommander.parse(args);
            if (appArgs.isHelp()) {
                throw new Exception("");
            }
        } catch (Exception e) {
            System.out.print(e.getMessage() + "\n\n");
            if (jCommander != null) {
                System.out.println("Usage like: java -jar 'game.jar' --size=20 --enemiesCount=2  --wallsCount=150\n");
                jCommander.usage();
                System.out.print("\nAvailable colors: "
                        + UnitType.availableColorNames()
                        .stream()
                        .sorted()
                        .collect(Collectors.joining(", "))
                        + "\n"
                );
            }
            System.exit(0);
        }

        return appArgs;
    }

    @Parameters(separators = "=")
    static class AppArgs {
        @Parameter(names = {"--help", "-h"}, description = "application usage description", help = true, order = 0)
        private boolean help = false;
        @Parameter(names = "--size", description = "Size of game field", required = true, order = 1)
        private int size;
        @Parameter(names = "--enemiesCount", description = "Count of enemy's", required = true, order = 2)
        private int enemiesCount;
        @Parameter(names = "--wallsCount", description = "Count of obstacles", required = true, order = 3)
        private int wallsCount;

        @Parameter(
                names = "--profile",
                description = "File with objects' char/color parameters. E.g [--profile=dev] equals to file [application-dev.properties]",
                order = 4
        )
        private String profile = "production";

        public boolean isHelp() {
            return help;
        }

        public void setHelp(boolean help) {
            this.help = help;
        }

        public int getEnemiesCount() {
            return enemiesCount;
        }

        public void setEnemiesCount(int enemiesCount) {
            this.enemiesCount = enemiesCount;
        }

        public int getWallsCount() {
            return wallsCount;
        }

        public void setWallsCount(int wallsCount) {
            this.wallsCount = wallsCount;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }
    }

    public static void setupUnitsParams(String profileName) throws Exception {
        String filePath = "/application-" + profileName + ".properties";
        try (InputStream inputStream = App.class.getResourceAsStream(filePath)) {
            Properties properties = new Properties();
            properties.load(inputStream);

            for (UnitType unit : UnitType.values()) {
                String name = unit.name().toLowerCase();

                String symbolProperty = properties.getProperty(name + ".char");
                char symbol = ' ';
                if (symbolProperty == null) {
                    throw new RuntimeException("[" + name + ".char]" + " parsing error");
                } else if (!symbolProperty.isEmpty()) {
                    symbol = symbolProperty.charAt(0);
                }
                unit.setSymbol(symbol);

                String colorName = properties.getProperty(unit.name().toLowerCase() + ".color");
                if (colorName == null || colorName.isEmpty()) {
                    throw new RuntimeException("[" + name + ".color]" + " parsing error");
                }
                unit.setColor(UnitType.attributeByName(colorName));
            }
        } catch (Exception e) {
            throw new RuntimeException(profileName + " profile loading: " + e.getMessage());
        }
    }

    private static void clearScreen() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("\n");
        }
        System.out.print(sb.toString());
    }
}
