import java.util.ArrayList;
import java.util.List;

public class HandlerChain {

    private final List<Handler> handlers;
    private Request request;

    public HandlerChain(Request request) {
        this.request = request;
        handlers = new ArrayList<>();
    }

    public void add(Handler handler) {
        handlers.add(handler);
    }

    public void process() {
        for (Handler handler : handlers) {
            Boolean result = handler.process(request);
            System.out.println(request.getName() + " 处理结果：" + result);
        }
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
