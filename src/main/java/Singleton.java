public class Singleton {
    private int num = 3;
    private static final Singleton instance = create();

    private static Singleton create() {
        System.out.println("instance1=" + instance);
        return new Singleton();
    }

    public static Singleton getInstance() {
        return instance;
    }

    static {
        System.out.println("init class");
        System.out.println("instance3=" + instance);
    }

    private Singleton() {
        System.out.println("init singleton");
        System.out.println(String.format("num:%s", num));
        System.out.println("instance2=" + instance);

    }
}
