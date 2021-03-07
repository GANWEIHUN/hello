import java.math.BigDecimal;

public class DirectorHandler implements Handler {
    @Override
    public Boolean process(Request request) {
        if (request.getCount().compareTo(new BigDecimal(1000)) < 0) {
            return true;
        }
        return null;
    }
}
