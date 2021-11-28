package observer;

/**
 * 产品被观察者接口
 */
public interface ProductObservable {
    void addObserver(ProductObserver observer);

    void removeObserver(ProductObserver observer);
}
