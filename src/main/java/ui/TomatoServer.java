package ui;

import common.Action1;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * @author tomato
 * @date 2021/03/04 15:50
 */
public class TomatoServer {
    private Action1<String> displayTextAction;
    private SocketType socketType;

    TomatoServer() {
        socketType = SocketType.TCP;//默认
    }

    public void start() throws IOException {
        if (SocketType.TCP == socketType) {
            initTCP();
        } else if (SocketType.UDP == socketType) {
            initUDP();
        }
    }

    private void initUDP() throws SocketException {
        DatagramSocket serviceSocket = new DatagramSocket(2021);//监听指定端口
        displayTextAction.invoke("启动服务成功！\n");
        for (; ; ) {
            try {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                serviceSocket.receive(packet);//接收数据报文包裹
                String msg = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);//取出包裹数据转换为字符串
                displayTextAction.invoke("接收到客户端请求：" + msg + "\n");
                byte[] data = msg.getBytes(StandardCharsets.UTF_8);
                packet.setData(data);
                serviceSocket.send(packet);//发送数据报文包裹
                if ("bye".equals(msg)) {
                    displayTextAction.invoke("客户端请求结束\n");
                }
            } catch (Exception e) {
                displayTextAction.invoke(e.getMessage());
                break;
            }
        }
    }

    private void initTCP() throws IOException {
        ServerSocket serverSocket = new ServerSocket(2021);//监听指定端口
        displayTextAction.invoke("启动服务成功！\n");
        while (true) {
            Socket client = serverSocket.accept();
            Thread thread1 = new Thread(new Handler(client, displayTextAction));
            thread1.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                client.close();
                displayTextAction.invoke("关闭客户端链接：" + exception.getMessage());
                exception.printStackTrace();
                break;
            }
        }
    }

    void setDisplayTextAction(Action1<String> displayTextAction) {
        this.displayTextAction = displayTextAction;
    }

    public void setSocketType(SocketType socketType) {
        this.socketType = socketType;
    }

    private static class Handler implements Runnable {
        private final Socket client;
        private final Action1<String> displayTextAction;

        private Handler(Socket socket, Action1<String> displayTextAction) {
            client = socket;
            this.displayTextAction = displayTextAction;
        }

        @Override
        public void run() {
            try (OutputStream outputStream = client.getOutputStream(); InputStream inputStream = client.getInputStream()) {
                //原理：服务端和客户端连接成功之后，通过inputStream,outputStream进行通信（数据交换）
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                bufferedWriter.write("ui.TomatoServer");
                bufferedWriter.newLine();
                bufferedWriter.flush();//主动推送给客户端
                while (true) {
                    try {
                        String line = bufferedReader.readLine();
                        displayTextAction.invoke("接收到客户端请求：" + line + "\n");
                        //回复客户端请求
                        bufferedWriter.write(line);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();//主动推送给客户端
                        if ("bye".equals(line)) {
                            displayTextAction.invoke("客户端断开链接" + "\n");
                            break;
                        }
                    } catch (Exception exception) {
                        displayTextAction.invoke(exception.getMessage() + "\n");
                        break;
                    }
                }
            } catch (IOException e) {
                displayTextAction.invoke(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
