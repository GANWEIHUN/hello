import car.Engine;

public class InhaleEngine implements Engine {
    private final String name;

    public InhaleEngine() {
        //吸气发动机
        this.name = "吸气发动机";
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
