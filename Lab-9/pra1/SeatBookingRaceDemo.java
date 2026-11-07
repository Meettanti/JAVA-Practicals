import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SeatBookingRaceDemo {
    private static final int INITIAL_SEATS = 5;
    private static final int CUSTOMER_COUNT = 10;

    public static void main(String[] args) throws InterruptedException {
        SeatBooking unsynchronizedBooking = new SeatBooking(INITIAL_SEATS);
        boolean[] unsynchronizedResults = runUnsynchronizedBookings(unsynchronizedBooking);
        int unsynchronizedSuccesses = countSuccessfulBookings(unsynchronizedResults);
        System.out.println("Unsynchronized bookings: " + unsynchronizedSuccesses
                + ", seats remaining: " + unsynchronizedBooking.getSeatsLeft());

        SeatBooking synchronizedBooking = new SeatBooking(INITIAL_SEATS);
        boolean[] synchronizedResults = runSynchronizedBookings(synchronizedBooking);
        int synchronizedSuccesses = countSuccessfulBookings(synchronizedResults);
        System.out.println("Synchronized bookings: " + synchronizedSuccesses
                + ", seats remaining: " + synchronizedBooking.getSeatsLeft());
    }

    private static boolean[] runUnsynchronizedBookings(SeatBooking booking)
            throws InterruptedException {
        boolean[] results = new boolean[CUSTOMER_COUNT];
        CyclicBarrier checkBarrier = new CyclicBarrier(CUSTOMER_COUNT);
        Thread[] threads = new Thread[CUSTOMER_COUNT];

        for (int i = 0; i < CUSTOMER_COUNT; i++) {
            int customerIndex = i;
            threads[i] = new Thread(() -> {
                try {
                    results[customerIndex] = booking.bookUnsynchronized(checkBarrier);
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                } catch (BrokenBarrierException exception) {
                    throw new IllegalStateException("Booking race barrier failed.", exception);
                }
            }, "UnsynchronizedCustomer-" + (i + 1));
            threads[i].start();
        }
        joinAll(threads);
        return results;
    }

    private static boolean[] runSynchronizedBookings(SeatBooking booking)
            throws InterruptedException {
        boolean[] results = new boolean[CUSTOMER_COUNT];
        Thread[] threads = new Thread[CUSTOMER_COUNT];

        for (int i = 0; i < CUSTOMER_COUNT; i++) {
            int customerIndex = i;
            threads[i] = new Thread(
                    () -> results[customerIndex] = booking.book(),
                    "SynchronizedCustomer-" + (i + 1));
            threads[i].start();
        }
        joinAll(threads);
        return results;
    }

    private static void joinAll(Thread[] threads) throws InterruptedException {
        for (Thread thread : threads) {
            thread.join();
        }
    }

    private static int countSuccessfulBookings(boolean[] results) {
        int successes = 0;
        for (boolean result : results) {
            if (result) {
                successes++;
            }
        }
        return successes;
    }

    private static class SeatBooking {
        private int seatsLeft;

        private SeatBooking(int seatsLeft) {
            this.seatsLeft = seatsLeft;
        }

        private boolean bookUnsynchronized(CyclicBarrier checkBarrier)
                throws InterruptedException, BrokenBarrierException {
            int remaining = seatsLeft;
            if (remaining <= 0) {
                return false;
            }
            checkBarrier.await();
            seatsLeft = remaining - 1;
            return true;
        }

        private synchronized boolean book() {
            if (seatsLeft <= 0) {
                return false;
            }
            seatsLeft--;
            return true;
        }

        private int getSeatsLeft() {
            return seatsLeft;
        }
    }
}
