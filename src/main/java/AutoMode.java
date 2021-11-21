import car.DriveMode;

public class AutoMode implements DriveMode {
    private final String name;

    public AutoMode() {
        //自动挡
        this.name = "自动挡";
    }

    @Override
    public DriveMode getDriveMode() {
        return this;
    }

    @Override
    public String toString() {
        return name;
    }
}
