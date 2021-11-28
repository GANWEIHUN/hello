package observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 商店
 */
public class Store implements ProductObservable {
    private final List<ProductObserver> productObservers;
    private final List<Product> products;

    public Store() {
        products = new ArrayList<>();
        productObservers = new ArrayList<>();
    }

    public void changePrice(Product product) {
        ProductEventArgs args = new ProductEventArgs(product, EventType.Price);
        publishEvent(args);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        ProductEventArgs args = new ProductEventArgs(product, EventType.Remove);
        publishEvent(args);
    }

    public void addNewProduct(Product product) {
        products.add(product);
        //上新产品
        ProductEventArgs args = new ProductEventArgs(product, EventType.New);
        publishEvent(args);
    }

    private void publishEvent(ProductEventArgs args) {
        for (ProductObserver observer : productObservers) {
            //try {
            //    observer.onEvent(args);
            //} catch (InterruptedException exception) {
            //    exception.printStackTrace();
            //}

            //注意：这里防止观察处理事件很慢，或者出现异常，导致后面的观察者接受不到事件，因此这里要异步处理
            Thread thread = new Thread(() -> {
                try {
                    observer.onEvent(args);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            });
            thread.start();
        }
    }

    @Override
    public void addObserver(ProductObserver observer) {
        productObservers.add(observer);
    }

    @Override
    public void removeObserver(ProductObserver observer) {
        productObservers.remove(observer);
    }
}
