import java.math.BigDecimal;

public class OverDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal getDiscount(BigDecimal value) {
        //满100减5块
        if (value.compareTo(new BigDecimal(100)) > 0) {
            return new BigDecimal(5);
        }
        return BigDecimal.ZERO;
    }
}
