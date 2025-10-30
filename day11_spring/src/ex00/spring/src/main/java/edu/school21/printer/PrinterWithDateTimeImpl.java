package edu.school21.printer;

import edu.school21.renderer.Renderer;

import java.time.LocalDateTime;

public class PrinterWithDateTimeImpl implements Printer {
    private final Renderer renderer;

    public PrinterWithDateTimeImpl(Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void print(Object obj) {
        LocalDateTime localDateTime = LocalDateTime.now();
        String str = localDateTime + ": " + obj;
        renderer.render(str);
    }
}
