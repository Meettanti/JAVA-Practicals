package service;

public class BankSession implements AutoCloseable {
    private final String name;

    public BankSession(String name) {
        this.name = name;
        System.out.println("Opened session: " + name);
    }

    public String getName() {
        return name;
    }

    @Override
    public void close() {
        System.out.println("Closed session: " + name);
    }
}
