import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
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
        try {
            Class<?> clazz = method.getDeclaringClass();
            String name = clazz.getName() + "Impl";
            Class<?> target = Class.forName(name);
            Object obj = target.newInstance();
            return method.invoke(obj, args);
        } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
