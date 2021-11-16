import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvocationHandler implements InvocationHandler {
    private final UserType userType;

    public MyInvocationHandler(UserType userType) {
        this.userType = userType;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        if (UserType.Ordinary == userType) {
            return "没有权限";
        }
        return "有权限";
    }
}
