import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal getDiscount(BigDecimal value);
}
