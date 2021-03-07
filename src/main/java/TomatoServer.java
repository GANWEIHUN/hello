import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author tomato
 * @date 2021/03/04 15:50
 */
public class TomatoServer {
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(2021);
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Socket client = serverSocket.accept();
                    System.out.println("新进消息");
                    Thread thread1 = new Thread(new HandleClient(client));
                    thread1.start();
                    Thread.sleep(1000);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //thread.setDaemon(true);//守护线程，保证和进程结束退出。
        thread.start();
        System.out.println("启动服务成功！");
    }

    public void reply(String text) {

    }

    private class HandleClient implements Runnable {

        private final Socket client;

        private HandleClient(Socket socket) {
            client = socket;
        }

        @Override
        public void run() {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
                Object obj = inputStream.readObject();
                if (obj instanceof String) {
                    System.out.println("接收到客户端请求：" + obj);
                }
                ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
                //回复客户端请求
                outputStream.writeObject("hello world");
                inputStream.close();
                outputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    client.close();
                    System.out.println("关闭客户端");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
