public class QueryServiceImpl implements QueryService {
    @Override
    public Object query(Object arg) {
        if ("A".equals(arg)) {
            return "分数100分";
        }
        if ("B".equals(arg)) {
            return "分数80分";
        }
        return "59分";
    }
}
