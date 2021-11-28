package observer;

/**
 * 产品观察者接口
 */
public interface ProductObserver {
    void onEvent(ProductEventArgs eventArgs) throws InterruptedException;
}
