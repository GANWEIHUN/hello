import java.util.List;

/**
 * @author tomato
 * @date 2020/11/13 11:22
 */
public class CommentNode implements Node {

    private final String text;

    public CommentNode(String text) {
        this.text = text;
    }

    @Override
    public Node add(Node node) {
        return this;
    }

    @Override
    public List<Node> children() {
        return null;
    }

    @Override
    public String toXml() {
        return "<!--" + text + "-->";
    }
}
