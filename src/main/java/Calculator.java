import java.util.concurrent.Callable;

/**
 * @author tomato
 * @date 2020/11/13 10:53
 */
public class Calculator implements Callable<Long> {

    private final long num;
    private long sum;

    public Calculator(long num) {
        this.num = num;
    }

    @Override
    public Long call() throws Exception {
        sum = 0;
        for (long i = 1; i < num; i++) {
            sum += i;
        }
        System.out.println("sum:" + sum);
        return sum;
    }

}
