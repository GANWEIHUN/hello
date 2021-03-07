/**
 * @author tomato
 * @date 2020/11/13 10:27
 */
public class SpanLabel implements TextLabel {

    private String text;

    @Override
    public String getText() {
        return "<span>" + text + "</span>";
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
}
