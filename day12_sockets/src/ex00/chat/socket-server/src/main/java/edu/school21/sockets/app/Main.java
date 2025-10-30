package edu.school21.sockets.app;

import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.server.Server;
import edu.school21.sockets.services.MessageService;
import edu.school21.sockets.services.UsersService;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.Optional;

public class Main {
    static int port = 8081;

    public static void main(String[] args) {
        System.out.println("args: " + String.join(" ", args));
        Optional<String> portArg = Arrays.stream(args).filter(s -> s.contains("port")).findFirst();
        if (portArg.isPresent()) {
            String[] argParts = portArg.get().split("=");
            if (argParts.length == 2) {
                port = Integer.parseInt(argParts[1]);
            }
        }

        try (ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
             Server server = new Server(
                     applicationContext.getBean(UsersService.class),
                     applicationContext.getBean(MessageService.class),
                     port)

        ) {
            System.out.println("Server starting listen port " + server.getPort() + " ...");
            server.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
