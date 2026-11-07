public class CinemaShow {
    private static int totalBooked = 0;

    private String title;
    private int seatsAvailable;
    private final int capacity;

    public CinemaShow(String title, int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative.");
        }
        this.title = title;
        this.capacity = capacity;
        seatsAvailable = capacity;
    }

    public CinemaShow(String title) {
        this(title, 100);
    }

    public boolean book(int numberOfSeats) {
        if (numberOfSeats < 0 || numberOfSeats > seatsAvailable) {
            return false;
        }
        seatsAvailable -= numberOfSeats;
        totalBooked += numberOfSeats;
        return true;
    }

    public void cancel(int numberOfSeats) {
        if (numberOfSeats < 0) {
            System.out.println("Cannot cancel a negative number of seats.");
            return;
        }
        long updatedSeats = (long) seatsAvailable + numberOfSeats;
        seatsAvailable = (int) Math.min(capacity, updatedSeats);
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public static int getTotalBooked() {
        return totalBooked;
    }

    public static void main(String[] args) {
        CinemaShow show = new CinemaShow("Practical 2 Demo", 10);

        boolean firstBooking = show.book(6);
        System.out.println("Book 6: " + firstBooking + ", seats available: " + show.getSeatsAvailable());

        boolean oversizedBooking = show.book(5);
        System.out.println("Book 5: " + oversizedBooking + ", seats available: " + show.getSeatsAvailable());

        show.cancel(3);
        System.out.println("Cancel 3, seats available: " + show.getSeatsAvailable());

        boolean finalBooking = show.book(4);
        System.out.println("Book 4: " + finalBooking + ", seats available: " + show.getSeatsAvailable());

        show.cancel(10);
        System.out.println("Cancel 10, seats available: " + show.getSeatsAvailable());

        CinemaShow defaultShow = new CinemaShow("Default Capacity Show");
        System.out.println("Default show seats: " + defaultShow.getSeatsAvailable());
        System.out.println("Total tickets booked: " + getTotalBooked());
    }
}
