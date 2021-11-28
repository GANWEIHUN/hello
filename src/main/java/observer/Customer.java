package observer;

import java.time.LocalTime;

/**
 * 消费者
 */
public class Customer implements ProductObserver {
    @Override
    public void onEvent(ProductEventArgs eventArgs) {
        System.out.println(LocalTime.now() + " 消费者关注产品信息 " + eventArgs.getEventType() + " " + eventArgs.getProduct().getName() + " " + eventArgs.getProduct().getPrice());
    }
}
