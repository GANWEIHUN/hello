import car.Engine;

public class TurboEngine implements Engine {
    private final String name;

    public TurboEngine() {
        //涡轮增压发动机
        this.name = "涡轮增压发动机";
    }

    @Override
    public Engine getEngine() {
        return this;
    }

    @Override
    public String toString() {
        return name;
    }
}
