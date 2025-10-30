package edu.school21.application;

import edu.school21.printer.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context.xml");

        Printer printer = applicationContext.getBean("printer", Printer.class);
        printer.print("Hello WORLD ^_^");
    }
}
