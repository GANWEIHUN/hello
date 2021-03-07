/**
 * @author tomato
 * @date 2020/11/13 10:31
 */
public abstract class LabelDecorator implements TextLabel {
    protected final TextLabel target;

    protected LabelDecorator(TextLabel textLabel) {
        this.target = textLabel;
    }

    public void setText(String text) {
        target.setText(text);
    }
}
