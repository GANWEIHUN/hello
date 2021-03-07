import java.math.BigDecimal;

public class ManagerHandler implements Handler {
    @Override
    public Boolean process(Request request) {
        if (request.getCount().compareTo(new BigDecimal(2000)) < 0) {
            if (request.getName().equals("张三")) {
                return false;
            }
            return true;
        }
        return null;
    }
}
