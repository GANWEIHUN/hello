package observer;

/**
 * 产品事件参数
 */
public class ProductEventArgs {
    private final Product product;
    private final EventType eventType;

    public ProductEventArgs(Product product, EventType eventType) {
        this.product = product;
        this.eventType = eventType;
    }

    public Product getProduct() {
        return product;
    }

    public EventType getEventType() {
        return eventType;
    }
}
