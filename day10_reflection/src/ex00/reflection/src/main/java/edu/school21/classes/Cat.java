package edu.school21.classes;

public class Cat {
    private String name;
    private int age;

    public Cat() {
        name = "Pushok";
        age = 3;
    }

    public Cat(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void say(String msg, int times) {
        for (int i = 0; i < times; i++) {
            System.out.println(name + " says: " + msg);
        }
    }

    public int grow(int plusYears) {
        age += plusYears;
        return age;
    }

    @Override
    public String toString() {
        return String.format("%s[name='%s', age=%d]", this.getClass().getSimpleName(), name, age);
    }
}
