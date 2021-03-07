public class FrontPosition implements EnginePosition {
    private final String name;

    public FrontPosition() {
        //前置发动机
        this.name = "前置发动机";
    }

    @Override
    public EnginePosition getEnginePosition() {
        return this;
    }

    @Override
    public String toString() {
        return name;
    }
}
