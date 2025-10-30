package edu.school21.printer.logic;

import java.io.IOException;

public class ConsolePrinter {
    private final ConsolePrintable printable;

    public ConsolePrinter(ConsolePrintable printable) {
        this.printable = printable;
    }

    public void load(String filePath) throws IOException, SecurityException {
        printable.load(filePath);
    }

    public void printInfo() {
        printable.printInfo();
    }

    /**
     * @param str пример последовательности:
     *            для черно/белого - "x." (где 'x' - черное, '.' - белое)
     */
    public void setPixelChar(String str) {
        printable.setPixelChar(str);
    }

    public void print() {
        printable.print();
    }
}