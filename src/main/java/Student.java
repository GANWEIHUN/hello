import myAnnotation.MyCheck;

/**
 * @author: tomato
 * @since: 2020/09/09 09:07
 * @Description:
 */
public class Student {

    //region fields
    @MyCheck(min = 1, max = 10)
    public String Name;

    @MyCheck(min = 1, max = 6)
    public String Gender;
    //endregion

    //region public methods

    //endregion

    //region constructor

    //endregion

    //region private methods

    //endregion

    //region overrides

    //endregion
}
