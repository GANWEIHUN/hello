/**
 * @author tomato
 * @date 2020/11/13 10:29
 */
public class BoldDecorator extends LabelDecorator {

    protected BoldDecorator(TextLabel textLabel) {
        super(textLabel);
    }

    @Override
    public String getText() {
        return "<bold>" + target.getText() + "</bold>";
    }
}
