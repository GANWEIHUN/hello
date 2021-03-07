abstract class Car {
    /**
     * 发动机
     *
     * @return
     */
    abstract Car setEngine(Engine engine);

    /**
     * 驱动模式
     *
     * @return
     */
    abstract Car setDriveMode(DriveMode mode);

    /**
     * 发动机位置
     *
     * @return
     */
    abstract Car setEnginePosition(EnginePosition position);

    /**
     * 轮胎
     *
     * @return
     */
    abstract Car setTyre(Tyre tyre);

    /**
     * 启动
     */
    abstract void run();


}
