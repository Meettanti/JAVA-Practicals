public class Light implements Switchable {
    private boolean on;

    @Override
    public void on() {
        on = true;
        System.out.println("Light is on");
    }

    @Override
    public void off() {
        on = false;
        System.out.println("Light is off");
    }

    @Override
    public boolean isOn() {
        return on;
    }
}
