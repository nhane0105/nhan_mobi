package com.example.baitap3;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientActivity extends AppCompatActivity {

    private TextView chatBox;
    private EditText messageInput;
    private Button sendButton;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isConnected = false;

    // ĐỔI IP NÀY THÀNH IP CỦA ĐIỆN THOẠI CHẠY SERVER
    private static final String SERVER_IP = "192.168.28.112";
    private static final int SERVER_PORT = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        chatBox = findViewById(R.id.chatBox);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        chatBox.setText("Đang kết nối đến " + SERVER_IP + ":" + SERVER_PORT + "...");

        new Thread(new ClientThread()).start();

        sendButton.setOnClickListener(v -> sendMessage());
    }

    class ClientThread implements Runnable {
        @Override
        public void run() {
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                isConnected = true;

                handler.post(() -> {
                    chatBox.append("\n✓ Đã kết nối thành công!");
                    Toast.makeText(ClientActivity.this, "Kết nối thành công!", Toast.LENGTH_SHORT).show();
                });

                // Lắng nghe tin nhắn từ server
                String message;
                while ((message = in.readLine()) != null) {
                    final String msg = message;
                    handler.post(() -> chatBox.append("\nServer: " + msg));
                }

            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> {
                    chatBox.append("\n✗ Lỗi kết nối: " + e.getMessage());
                    Toast.makeText(ClientActivity.this, "Không thể kết nối: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }
    }

    private void sendMessage() {
        String message = messageInput.getText().toString().trim();

        if (!isConnected) {
            Toast.makeText(this, "Chưa kết nối đến server!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (message.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tin nhắn!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gửi tin nhắn trong background thread
        new Thread(() -> {
            try {
                out.println(message);
                handler.post(() -> {
                    chatBox.append("\nBạn: " + message);
                    messageInput.setText("");
                });
            } catch (Exception e) {
                handler.post(() -> Toast.makeText(ClientActivity.this, "Lỗi gửi tin: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (socket != null) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
