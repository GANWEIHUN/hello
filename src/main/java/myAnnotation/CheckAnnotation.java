package myAnnotation;

import com.sun.istack.internal.NotNull;

import java.lang.reflect.Field;

public class CheckAnnotation {

    public static <T> boolean check(@NotNull T t) throws IllegalAccessException {
        boolean result = true;
        for (Field field : t.getClass().getFields()) {
            MyCheck myCheck = field.getAnnotation(MyCheck.class);
            if (myCheck != null) {
                Object value = field.get(t);
                if (value instanceof String) {
                    String s = String.valueOf(value);
                    if (s.length() < myCheck.min() || s.length() > myCheck.max()) {
                        result = false;
                        System.out.println(String.format("字段%s 长度超出范围[%s,%s]", field, myCheck.min(), myCheck.max()));
                    }
                }
            }
        }
        return result;
    }
}
