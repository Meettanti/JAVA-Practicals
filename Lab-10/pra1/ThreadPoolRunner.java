import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolRunner {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int taskId = 1; taskId <= 10; taskId++) {
            int currentTaskId = taskId;
            executor.submit(() -> runTask(currentTaskId));
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                throw new IllegalStateException("Thread-pool tasks did not finish in time.");
            }
        } catch (InterruptedException exception) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread-pool runner was interrupted.", exception);
        }
    }

    private static void runTask(int taskId) {
        System.out.println("Task " + taskId + " running on "
                + Thread.currentThread().getName());
        try {
            Thread.sleep(100);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            System.out.println("Task " + taskId + " was interrupted.");
        }
    }
}
