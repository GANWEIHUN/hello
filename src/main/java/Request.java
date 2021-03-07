import java.math.BigDecimal;

public class Request {
    private final BigDecimal count;
    private final String name;

    public Request(String name, BigDecimal count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getCount() {
        return count;
    }
}
