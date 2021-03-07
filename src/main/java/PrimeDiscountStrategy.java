import java.math.BigDecimal;

public class PrimeDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal getDiscount(BigDecimal value) {
        //会员9折
        value = value.multiply(new BigDecimal("0.1"));
        return value;
    }
}
