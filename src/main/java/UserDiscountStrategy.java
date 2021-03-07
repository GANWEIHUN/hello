import java.math.BigDecimal;

public class UserDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal getDiscount(BigDecimal value) {
        //普通用户99折
        value = value.multiply(new BigDecimal("0.01"));
        return value;
    }
}
