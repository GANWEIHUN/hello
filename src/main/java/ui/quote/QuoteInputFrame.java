package ui.quote;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 墨水屏输入窗口
 *
 * @author tomato
 * @date 2025/9/26 11:20
 */
public class QuoteInputFrame extends JFrame {
    private static final String API_URL = "https://dot.mindreset.tech/api/open/text";
    private static final String API_KEY = System.getenv("QUOTE_API_KEY");// 替换实际 API key
    private final JLabel tipLabel;

    public QuoteInputFrame() {
        setSize(400, 500);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTextArea inputTextArea = new JTextArea();
        inputTextArea.requestFocus();
        contentPane.add(inputTextArea, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout(1, 1));
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        tipLabel = new JLabel("发送状态");
        bottomPanel.add(tipLabel, BorderLayout.NORTH);

        JButton button = new JButton("发送");
        button.addActionListener(e -> {
            String text = inputTextArea.getText();
            senderMsg(text);
            inputTextArea.setText("");
        });
        bottomPanel.add(button, BorderLayout.SOUTH);
    }

    private HttpPost buildRequest(String text) {
        //官方API文档：https://dot.mindreset.tech/docs/server/template/api/text_api
        HttpPost request = new HttpPost(API_URL);
        request.addHeader("Authorization", "Bearer " + API_KEY);
        request.addHeader("Content-Type", "application/json");

        // 构建结构化请求体
        JsonObject body = new JsonObject();
        body.addProperty("deviceId", "48F6EE55ADAC");
        body.addProperty("title", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        body.addProperty("message", text);
        body.addProperty("signature", "tomato");
        request.setEntity(new StringEntity(body.toString(), StandardCharsets.UTF_8));
        return request;
    }

    private void senderMsg(String text) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = buildRequest(text);
            CloseableHttpResponse httpResponse = httpClient.execute(request);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                //正确响应
                InputStream inputStream = httpResponse.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    JsonObject responseJson = JsonParser.parseString(line).getAsJsonObject();
                    showTip(responseJson.get("message").getAsString());
                }
            } else {
                showTip("请求失败，状态码: " + httpResponse.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("请求失败", e);
        }
    }

    private void showTip(String tip) {
        tipLabel.setText(tip);
    }

    public static void main(String[] args) {
        QuoteInputFrame quoteInputFrame = new QuoteInputFrame();
        quoteInputFrame.setLocationRelativeTo(null);//居中显示
        quoteInputFrame.setVisible(true);
    }
}
