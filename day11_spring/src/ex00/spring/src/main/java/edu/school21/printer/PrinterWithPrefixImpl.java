package edu.school21.printer;

import edu.school21.renderer.Renderer;

public class PrinterWithPrefixImpl implements Printer {
    private final Renderer renderer;
    private String prefix;

    public PrinterWithPrefixImpl(Renderer renderer, String prefix) {
        this.renderer = renderer;
        this.prefix = prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void print(Object obj) {
        String str = prefix + " " + obj;
        renderer.render(str);
    }
}
