import java.util.List;

/**
 * @author tomato
 * @date 2020/11/13 11:16
 */
interface Node {
    Node add(Node node);

    List<Node> children();

    String toXml();
}
