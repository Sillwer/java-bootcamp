package edu.school21.chat.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;

import edu.school21.chat.models.Message;
import edu.school21.chat.models.User;
import edu.school21.chat.repositories.MessagesRepository;
import edu.school21.chat.repositories.MessagesRepositoryJdbcImpl;

import edu.school21.chat.repositories.UsersRepository;
import edu.school21.chat.repositories.UsersRepositoryJdbcImpl;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Program {
    static String dbHost = "localhost";
    static String dbPort = "5432";
    static String dbName = "chat_s21";
    static String dbUserName = "postgres";
    static String dbPassword = "";
    static final String STOP_WORD = "exit";

    public static void main(String[] args) {
        setupArgs(args);

        HikariDataSource dataSource = getDataSource();
        UsersRepository usersRepository = new UsersRepositoryJdbcImpl(dataSource);

        try (Scanner sc = new Scanner(System.in)) {
            String input = "";
            System.out.println("\n>>> TO EXIT INPUT: " + STOP_WORD);
            while (true) {
                System.out.print("Input [size_of_page] [number_of_page]: ");
                input = sc.nextLine().trim();
                if (input.equalsIgnoreCase(STOP_WORD)) {
                    break;
                }

                Scanner scLine = new Scanner(input);
                int pageSize = scLine.nextInt();
                int pageNumber = scLine.nextInt();

                List<User> users = usersRepository.findAll(pageNumber, pageSize);

                System.out.println("\nfound users {");
                for (User u : users) {
                    System.out.println(u);
                }
                System.out.print("}\n\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        dataSource.close();
        System.out.println("Goodbye");
    }

    private static void setupArgs(String[] args) {
        Options options = new Options();
        options.addOption("h", "host", true, "database host");
        options.addOption("p", "port", true, "database connection port");
        options.addOption("d", "dataBase", true, "database name");
        options.addOption("U", "user", true, "DBMS user name");
        options.addOption("P", "password", true, "DBMS user password");

        try {
            DefaultParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);

            if (commandLine.hasOption("h")) {
                dbHost = commandLine.getOptionValue("h");
            }
            if (commandLine.hasOption("p")) {
                dbPort = commandLine.getOptionValue("p");
            }
            if (commandLine.hasOption("d")) {
                dbName = commandLine.getOptionValue("d");
            }
            if (commandLine.hasOption("U")) {
                dbUserName = commandLine.getOptionValue("U");
            }
            if (commandLine.hasOption("P")) {
                dbPassword = commandLine.getOptionValue("P");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static HikariDataSource getDataSource() {
        String JdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", dbHost, dbPort, dbName);

        try (Connection connection = DriverManager.getConnection(JdbcUrl, dbUserName, dbPassword)) {
            if (connection.isValid(10)) {
                System.out.printf("Connection [%s:%s] to database [%s] as [%s] is available.\n", dbHost, dbPort, dbName, dbUserName);
            }
        } catch (SQLException e) {
            System.out.printf("Can't connect %s:%s to the database %s as %s.\n%s", dbHost, dbPort, dbName, dbUserName, e.getMessage());
            System.exit(2);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JdbcUrl);
        config.setUsername(dbUserName);
        config.setPassword(dbPassword);
        return new HikariDataSource(config);
    }
}
