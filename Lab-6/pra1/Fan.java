public class Fan implements Switchable {
    private boolean on;

    @Override
    public void on() {
        on = true;
        System.out.println("Fan is on");
    }

    @Override
    public void off() {
        on = false;
        System.out.println("Fan is off");
    }

    @Override
    public boolean isOn() {
        return on;
    }
}
