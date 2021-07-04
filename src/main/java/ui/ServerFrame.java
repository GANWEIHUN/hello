package ui;

import common.Action1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

/**
 * @author tomato
 * @date 2021/03/04 18:05
 */
public class ServerFrame extends JFrame {

    private final JTextArea displayTextArea;

    private ServerFrame() {
        setTitle("tomato服务端");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLocationRelativeTo(null);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        displayTextArea = new JTextArea();
        contentPane.add(displayTextArea);
        String tex = "发送";
        JButton button1 = new JButton(tex);    //创建JButton对
        button1.setEnabled(false);
        button1.setFont(new Font("黑体", Font.BOLD, 16));    //修改字体样式
        button1.addActionListener(e -> senderMsg());
        contentPane.add(button1, BorderLayout.SOUTH);
    }

    private void senderMsg() {
    }

    public static void main(String[] args) {
        ServerFrame frame = new ServerFrame();
        frame.setVisible(true);
        frame.startServer();
    }

    private void startServer() {
        TomatoServer server = new TomatoServer();
        try {
            Action1<String> displayTextAction = displayTextArea::append;
            server.setDisplayTextAction(displayTextAction);
            //server.setSocketType(SocketType.UDP);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
