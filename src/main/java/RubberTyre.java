public class RubberTyre implements Tyre {
    private final String name;

    public RubberTyre() {
        //橡胶轮胎
        this.name = "橡胶轮胎";
    }

    @Override
    public Tyre getTyre() {
        return this;
    }

    @Override
    public String toString() {
        return name;
    }
}
