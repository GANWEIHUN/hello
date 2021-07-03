package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author tomato
 * @date 2021/03/04 16:58
 */
public class ClientFrame extends JFrame {

    private final JTextArea displayTextArea;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private ClientFrame(String[] args) {
        setTitle("tomato客户端");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLocationRelativeTo(null);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        displayTextArea = new JTextArea();
        displayTextArea.setEditable(false);
        displayTextArea.setBackground(new Color(255, 250, 227));
        if (args != null) {
            for (String arg : args) {
                displayTextArea.append(arg);
            }
        }
        contentPane.add(displayTextArea);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        bottomPanel.setLayout(new BorderLayout(0, 0));
        bottomPanel.setSize(0, 40);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        JTextField inputTextField = new JFormattedTextField();
        inputTextField.requestFocus();
        bottomPanel.add(inputTextField);
        String tex = "发送";
        JButton button1 = new JButton(tex);    //创建JButton对
        button1.setSize(100, 0);
        button1.setFont(new Font("黑体", Font.BOLD, 16));    //修改字体样式
        button1.addActionListener(e -> {
            String text = inputTextField.getText();
            inputTextField.setText("");
            senderMsg(text);
        });
        bottomPanel.add(button1, BorderLayout.EAST);
    }

    public static void main(String[] args) {
        ClientFrame frame = new ClientFrame(args);
        frame.setVisible(true);
        frame.connection();
    }

    private void senderMsg(String text) {
        if (socket == null || socket.isClosed()) {
            connection();
        }
        try {
            bufferedWriter.write(text);
            bufferedWriter.newLine();//必须加上这句，否则不能连续通信
            bufferedWriter.flush();//主动推送到服务器
            displayTextArea.append("收到服务端回复：" + bufferedReader.readLine() + "\n");
            if ("bye".equals(text)) {
                bufferedReader.close();
                bufferedWriter.close();
                socket.close();
                displayTextArea.append("断开链接！/n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            displayTextArea.append(e.getMessage());
        }
    }

    private void connection() {
        try {
            socket = new Socket("localhost", 2021);//链接到指定服务器和端口
            displayTextArea.append("链接服务器成功！");
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            displayTextArea.append("服务器名称：" + bufferedReader.readLine() + "\n");
        } catch (IOException e) {
            displayTextArea.append(e.getMessage());
            e.printStackTrace();
        }
    }
}
