package car;

public class Car {
    private Engine engine;
    private DriveMode driveMode;
    private EnginePosition enginePosition;
    private Tyre tyre;

    protected Car() {

    }

    /**
     * 发动机
     */
    protected void setEngine(Engine engine) {
        this.engine = engine;
    }

    /**
     * 驱动模式
     */
    protected void setDriveMode(DriveMode mode) {
        this.driveMode = mode;
    }

    /**
     * 发动机位置
     */
    protected void setEnginePosition(EnginePosition position) {
        this.enginePosition = position;
    }

    /**
     * 轮胎
     */
    protected void setTyre(Tyre tyre) {
        this.tyre = tyre;
    }

    /**
     * 启动
     */
    public void run() {
        System.out.printf("汽车启动 引擎:%s 引擎位置:%s 驱动方式:%s 轮胎:%s%n", engine, enginePosition, driveMode, tyre);
    }
}
