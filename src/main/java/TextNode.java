import java.util.List;

/**
 * @author tomato
 * @date 2020/11/13 11:21
 */
public class TextNode implements Node {

    private final String text;

    public TextNode(String text) {
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
        return text;
    }
}
