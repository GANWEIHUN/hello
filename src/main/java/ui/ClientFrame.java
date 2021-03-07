package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author tomato
 * @date 2021/03/04 16:58
 */
public class ClientFrame extends JFrame {

    private final JTextArea textArea;

    private ClientFrame(String[] args) {
        setTitle("tomato客户端");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        textArea = new JTextArea();
        textArea.append("我是文本！");
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                textArea.append(args[i]);
            }
        }
        contentPane.add(textArea);
        String tex = "发送";
        JButton button1 = new JButton(tex);    //创建JButton对
        button1.setFont(new Font("黑体", Font.BOLD, 16));    //修改字体样式
        button1.addActionListener(e -> {
            String text = textArea.getText();
            senderMsg(text);
        });
        contentPane.add(button1, BorderLayout.SOUTH);
        //initClient();
    }

    public static void main(String[] args) {
        ClientFrame frame = new ClientFrame(args);
        frame.setVisible(true);
    }

    private void initClient() {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 2021);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject("我是tomato");
            //outputStream.close();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Object result = inputStream.readObject();
            System.out.println("新进答复！" + result);
            if (result instanceof String) {
                if (((String) result).contains("ok")) {
                    textArea.append("成功接收服务端返回信息，结束此次请求！");
                }
            }
            //inputStream.close();
            Thread.sleep(1000);
            //socket.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void senderMsg(String text) {
        initClient();
    }
}
