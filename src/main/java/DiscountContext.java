import java.math.BigDecimal;

public class DiscountContext {

    private DiscountStrategy discountStrategy;

    public DiscountContext() {
        discountStrategy = new UserDiscountStrategy();
    }

    public void setDiscountStrategy(DiscountStrategy strategy) {
        discountStrategy = strategy;
    }

    public BigDecimal calculateDiscount(BigDecimal total) {
        return total.subtract(discountStrategy.getDiscount(total));
    }
}
