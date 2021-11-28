package observer;

import java.time.LocalTime;

/**
 * 管理员
 */
public class Admin implements ProductObserver {
    @Override
    public void onEvent(ProductEventArgs eventArgs) {
        try {
            //int a = 0;
            //int b = 1 / a;
            System.out.println(LocalTime.now() + " 管理员关注产品信息 " + eventArgs.getEventType() + " " + eventArgs.getProduct().getName() + " " + eventArgs.getProduct().getPrice());
            Thread.sleep(1000);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
