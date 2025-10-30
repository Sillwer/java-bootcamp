package edu.school21.printer.logic;

import java.io.IOException;
import java.io.InputStream;

public interface ConsolePrintable {
    void load(InputStream inputStream) throws IOException, SecurityException;

    void printInfo();

    void setPixelChar(String str);

    void print();
}
