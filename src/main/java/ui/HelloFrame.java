package ui;

import org.junit.Test;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Random;

public class HelloFrame extends JFrame {
    JLabel label;
    JButton button1;
    int clicks = 0;
    private String bigString = "";
    private final Random random;

    public HelloFrame() {
        setTitle("动作事件监听器示例");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        label = new JLabel(" ");
        label.setFont(new Font("楷体", Font.BOLD, 16));    //修改字体样式
        contentPane.add(label);
        String tex = "我是普通按钮";
        button1 = new JButton(tex);    //创建JButton对
        button1.setFont(new Font("黑体", Font.BOLD, 16));    //修改字体样式
        button1.addActionListener(e -> {
            label.setText("按钮被单击了 " + (++clicks) + " 次");
            buildString();
            label.setText(String.format("%s \n bigString长度：%s", label.getText(), bigString.length()));
        });
        contentPane.add(button1, BorderLayout.SOUTH);
        random = new Random();

    }

    private void buildString() {
        for (int i = 0; i < 100; i++) {
            String temp = String.format("%s%s", random.nextInt(), "你");
            bigString += temp;
        }
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }


    public static void main(String[] args) {
        HelloFrame frame = new HelloFrame();
        frame.setVisible(true);
    }

    @Test
    public void myTest() {

    }
}