import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author tomato
 * @date 2021/05/06 13:53
 */
public class StudentInfo {
    @XStreamAsAttribute
    private String code;
    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private int age;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
