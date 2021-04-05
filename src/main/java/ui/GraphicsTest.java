package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GraphicsTest extends JFrame {
    GraphicsTest() {
        setTitle("测试Graphics2D");
        setSize(800, 700);
        setLocationRelativeTo(null);
        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 250, 227));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        MyJPane myJPane = new MyJPane();
        JButton jButton = new JButton();
        //jButton.setBounds(10, 10, 100, 30);
        jButton.setText("刷新");
        jButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //myJPane.invalidate();
                myJPane.repaint();
            }
        });
        contentPane.add(jButton, BorderLayout.NORTH);
        contentPane.add(myJPane, BorderLayout.CENTER);
    }

    private class MyJPane extends JPanel {
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D graphics2D = (Graphics2D) g.create();
            try {
                graphics2D.setColor(Color.black);
                BasicStroke basicStroke = new BasicStroke(1);
                graphics2D.setStroke(basicStroke);
                graphics2D.drawRect(10, 10, getWidth() - 20, getHeight() - 20);
            } finally {
                graphics2D.dispose();
            }
        }
    }

    public static void main(String[] args) {
        GraphicsTest graphicsTest = new GraphicsTest();
        graphicsTest.setVisible(true);
    }
}


