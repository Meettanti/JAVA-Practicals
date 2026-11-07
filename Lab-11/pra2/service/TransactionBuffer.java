package service;

import java.util.Objects;

public class TransactionBuffer {
    private final Runnable[] transactions;
    private int head;
    private int tail;
    private int size;
    private boolean closed;

    public TransactionBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Buffer capacity must be positive.");
        }
        transactions = new Runnable[capacity];
    }

    public boolean put(Runnable transaction) throws InterruptedException {
        Objects.requireNonNull(transaction, "Transaction cannot be null.");
        synchronized (this) {
            while (size == transactions.length && !closed) {
                wait();
            }
            if (closed) {
                return false;
            }
            transactions[tail] = transaction;
            tail = (tail + 1) % transactions.length;
            size++;
            notify();
            return true;
        }
    }

    public Runnable take() throws InterruptedException {
        synchronized (this) {
            while (size == 0 && !closed) {
                wait();
            }
            if (size == 0) {
                return null;
            }
            Runnable transaction = transactions[head];
            transactions[head] = null;
            head = (head + 1) % transactions.length;
            size--;
            notify();
            return transaction;
        }
    }

    public void close() {
        synchronized (this) {
            closed = true;
            notifyAll();
        }
    }
}
