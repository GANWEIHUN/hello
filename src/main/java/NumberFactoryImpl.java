import java.math.BigDecimal;

public class NumberFactoryImpl extends NumberFactory {
    @Override
    Number parse(String s) {
        return new BigDecimal(s);
    }
}
