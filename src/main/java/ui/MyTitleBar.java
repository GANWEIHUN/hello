package ui;

import javax.swing.*;
import java.awt.*;

public class MyTitleBar extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g.create();
        try {
            graphics2D.setColor(new Color(246, 180, 192));
            graphics2D.fillRect(0, 0, getWidth(), getHeight());
            graphics2D.setColor(new Color(0, 57, 142));
            graphics2D.drawString("我是标题栏", getWidth() / 2, getHeight() / 2);
            graphics2D.dispose();
        } catch (Exception exception) {
            throw exception;
        }
    }

}