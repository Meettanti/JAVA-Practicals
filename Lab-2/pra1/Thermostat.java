public class Thermostat {
    private static final int MIN = 16;
    private static final int MAX = 30;
    private static int activeCount = 0;

    private String location;
    private int temperature;

    public Thermostat(String location, int startTemp) {
        this.location = location;
        if (startTemp >= MIN && startTemp <= MAX) {
            temperature = startTemp;
        } else {
            temperature = 22;
        }
        activeCount++;
    }

    public Thermostat(String location) {
        this(location, 22);
    }

    public void raise() {
        if (temperature < MAX) {
            temperature++;
        } else {
            System.out.println("Already at maximum (30)");
        }
    }

    public void lower() {
        if (temperature > MIN) {
            temperature--;
        } else {
            System.out.println("Already at minimum (16)");
        }
    }

    public int getTemperature() {
        return temperature;
    }

    public static int getActiveCount() {
        return activeCount;
    }

    public static void main(String[] args) {
        Thermostat livingRoom = new Thermostat("Living room", 25);
        Thermostat bedroom = new Thermostat("Bedroom");
        System.out.println(bedroom.location + " initial temperature: " + bedroom.getTemperature());

        for (int i = 0; i < 10; i++) {
            livingRoom.raise();
            System.out.println(livingRoom.location + " temperature: " + livingRoom.getTemperature());
        }

        for (int i = 0; i < 20; i++) {
            livingRoom.lower();
            System.out.println(livingRoom.location + " temperature: " + livingRoom.getTemperature());
        }

        System.out.println("Active thermostats: " + getActiveCount());
    }
}
