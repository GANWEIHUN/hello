import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author tomato
 * @date 2020/11/13 11:18
 */
public class ElementNode implements Node {

    private final List<Node> nodes;
    private final String name;

    public ElementNode(String name) {
        this.name = name;
        nodes = new ArrayList<>();
    }

    @Override
    public Node add(Node node) {
        nodes.add(node);
        return this;
    }

    @Override
    public List<Node> children() {
        return nodes;
    }

    @Override
    public String toXml() {
        String start = "<" + name + ">\n";
        String end = "</" + name + ">\n";
        StringJoiner stringJoiner = new StringJoiner("", start, end);
        for (Node node : nodes) {
            stringJoiner.add(node.toXml() + "\n");
        }
        return stringJoiner.toString();
    }
}
