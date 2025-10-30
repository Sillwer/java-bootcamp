package edu.school21.classes;

public class Product {
    private String name;
    private int price;

    public Product() {
        name = "Laptop";
        price = 666;
    }

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Product getCopy() {
        return new Product(name, price);
    }

    public int discount(int percent) {
        price *= 1 - (int) (1.f / percent / 10);
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s[name='%s', age=%d}", this.getClass().getSimpleName(), name, price);
    }
}
