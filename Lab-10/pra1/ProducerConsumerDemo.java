public class ProducerConsumerDemo {
    private static final int ITEM_COUNT = 10;

    public static void main(String[] args) throws InterruptedException {
        NumberBuffer buffer = new NumberBuffer(3);
        Thread producer = new Thread(() -> {
            try {
                for (int number = 1; number <= ITEM_COUNT; number++) {
                    if (!buffer.put(number)) {
                        break;
                    }
                    System.out.println("Produced " + number);
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            } finally {
                buffer.close();
            }
        }, "NumberProducer");

        Thread consumer = new Thread(() -> {
            try {
                Integer number;
                while ((number = buffer.take()) != null) {
                    System.out.println("Consumed " + number);
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            } finally {
                buffer.close();
            }
        }, "NumberConsumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }

    private static class NumberBuffer {
        private final int[] values;
        private int head;
        private int tail;
        private int size;
        private boolean closed;

        private NumberBuffer(int capacity) {
            values = new int[capacity];
        }

        private boolean put(int value) throws InterruptedException {
            synchronized (this) {
                while (size == values.length && !closed) {
                    wait();
                }
                if (closed) {
                    return false;
                }
                values[tail] = value;
                tail = (tail + 1) % values.length;
                size++;
                notify();
                return true;
            }
        }

        private Integer take() throws InterruptedException {
            synchronized (this) {
                while (size == 0 && !closed) {
                    wait();
                }
                if (size == 0) {
                    return null;
                }
                int value = values[head];
                head = (head + 1) % values.length;
                size--;
                notify();
                return value;
            }
        }

        private void close() {
            synchronized (this) {
                closed = true;
                notifyAll();
            }
        }
    }
}
