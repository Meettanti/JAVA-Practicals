@FunctionalInterface
public interface SwitchPermission {
    boolean maySwitchOn(Switchable device, int hour);
}
