public class MyCar extends Car {
    private Engine engine;
    private DriveMode mode;
    private EnginePosition enginePosition;
    private Tyre tyre;

    @Override
    Car setEngine(Engine engine) {
        this.engine = engine;
        return this;
    }

    @Override
    Car setDriveMode(DriveMode mode) {
        this.mode = mode;
        return this;
    }

    @Override
    Car setEnginePosition(EnginePosition position) {
        this.enginePosition = position;
        return this;
    }

    @Override
    Car setTyre(Tyre tyre) {
        this.tyre = tyre;
        return this;
    }

    @Override
    void run() {
        System.out.println("engine:" + engine);
        System.out.println("tyre:" + tyre);
        System.out.println("enginePosition:" + enginePosition);
        System.out.println("mode:" + mode);
        System.out.println("汽车启动");
    }
}
