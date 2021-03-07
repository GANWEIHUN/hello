package ui;

import javax.swing.*;
import java.awt.*;

public class MyContent extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g.create();
        try {
            graphics2D.setColor(new Color(133, 204, 219));
            graphics2D.fillRect(0, 0, getWidth(), getHeight());
            graphics2D.setColor(new Color(219, 88, 96));
            graphics2D.drawString("我是内容区域", getWidth() / 2, getHeight() / 2);
            graphics2D.dispose();
        } catch (Exception exception) {
            throw exception;
        }
    }
}
