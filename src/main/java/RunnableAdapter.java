import java.util.concurrent.Callable;

/**
 * @author tomato
 * @date 2020/11/13 10:58
 */
public class RunnableAdapter implements Runnable {

    private final Callable callable;

    public RunnableAdapter(Callable callable) {
        this.callable = callable;
    }

    @Override
    public void run() {
        try {
            callable.call();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
