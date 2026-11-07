public class CounterRaceDemo {
    private static final int THREAD_COUNT = 10;
    private static final int INCREMENTS_PER_THREAD = 1000;

    public static void main(String[] args) throws InterruptedException {
        int expected = THREAD_COUNT * INCREMENTS_PER_THREAD;

        Counter unsynchronizedCounter = new Counter();
        runWorkers(unsynchronizedCounter, false);
        System.out.println("Unsynchronized total: " + unsynchronizedCounter.getValue()
                + " (expected " + expected + ")");

        Counter synchronizedCounter = new Counter();
        runWorkers(synchronizedCounter, true);
        System.out.println("Synchronized total: " + synchronizedCounter.getValue()
                + " (expected " + expected + ")");
    }

    private static void runWorkers(Counter counter, boolean useSynchronization)
            throws InterruptedException {
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    if (useSynchronization) {
                        counter.incrementSynchronized();
                    } else {
                        counter.incrementUnsynchronized();
                    }
                }
            }, "CounterWorker-" + (i + 1));
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

    private static class Counter {
        private int value;

        private void incrementUnsynchronized() {
            int previous = value;
            Thread.yield();
            value = previous + 1;
        }

        private synchronized void incrementSynchronized() {
            value++;
        }

        private int getValue() {
            return value;
        }
    }
}
