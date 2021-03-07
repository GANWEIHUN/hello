public abstract class NumberFactory {
    abstract Number parse(String s);

    private static NumberFactoryImpl numberFactory = new NumberFactoryImpl();

    public static NumberFactoryImpl getFactory() {
        return numberFactory;
    }

}
