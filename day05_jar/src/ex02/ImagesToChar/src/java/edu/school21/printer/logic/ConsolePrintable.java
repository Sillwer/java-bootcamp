package edu.school21.printer.logic;

import java.io.IOException;
import java.io.InputStream;

public interface ConsolePrintable {
    void load(InputStream inputStream) throws IOException, SecurityException;

    void printInfo();

    void setPixelColors(String[] str);

    void print();
}
