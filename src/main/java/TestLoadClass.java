/**
 * @author tomato
 * @date 2021/04/22 13:20
 */
public class TestLoadClass {

    private static int a;

    static {
        System.out.printf("loadClass a被赋值了吗？a=%d%n", a);
    }

    public TestLoadClass() {
        a = 3;
    }
}
