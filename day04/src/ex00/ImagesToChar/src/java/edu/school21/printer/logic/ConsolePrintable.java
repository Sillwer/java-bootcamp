package edu.school21.printer.logic;

import java.io.IOException;

public interface ConsolePrintable {
    void load(String filePath) throws IOException, SecurityException;

    void printInfo();

    void setPixelChar(String str);

    void print();
}
