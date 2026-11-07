public class RemoteControlDemo {
    public static void main(String[] args) {
        Fan fan = new Fan();
        Light light = new Light();
        Switchable[] devices = {fan, light};

        for (Switchable device : devices) {
            device.toggle();
        }

        SwitchPermission daytimePermission = new SwitchPermission() {
            @Override
            public boolean maySwitchOn(Switchable device, int hour) {
                return device != null && hour >= 8 && hour < 18;
            }
        };
        SwitchPermission eveningPermission =
                (device, hour) -> device != null && hour >= 18 && hour < 22;

        System.out.println("Anonymous rule allows fan at 10: "
                + daytimePermission.maySwitchOn(fan, 10));
        System.out.println("Lambda rule allows light at 20: "
                + eveningPermission.maySwitchOn(light, 20));
    }
}
