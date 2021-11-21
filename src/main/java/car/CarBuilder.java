package car;

public class CarBuilder {

    private static CarBuilder builder;

    private CarBuilder() {

    }

    public static CarBuilder getBuilder() {
        if (builder == null) {
            builder = new CarBuilder();
        }
        return builder;
    }

    private Engine engine;
    private DriveMode mode;
    private EnginePosition enginePosition;
    private Tyre tyre;


    /**
     * 引擎
     *
     * @param engine
     * @return
     */
    public CarBuilder setEngine(Engine engine) {
        this.engine = engine;
        return this;
    }


    /**
     * 驱动方式
     *
     * @param mode
     * @return
     */
    public CarBuilder setDriveMode(DriveMode mode) {
        this.mode = mode;
        return this;
    }

    /**
     * 引擎位置
     *
     * @param position
     * @return
     */
    public CarBuilder setEnginePosition(EnginePosition position) {
        this.enginePosition = position;
        return this;
    }

    /**
     * 轮胎
     *
     * @param tyre
     * @return
     */
    public CarBuilder setTyre(Tyre tyre) {
        this.tyre = tyre;
        return this;
    }

    public Car build() {
        Car car = new Car();
        car.setEngine(engine);
        car.setEnginePosition(enginePosition);
        car.setDriveMode(mode);
        car.setTyre(tyre);
        return car;
    }
}
