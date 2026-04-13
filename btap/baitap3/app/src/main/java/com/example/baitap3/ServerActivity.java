package com.example.baitap3;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

public class ServerActivity extends AppCompatActivity {

    private TextView chatBox;
    private Handler handler = new Handler(Looper.getMainLooper());
    private ServerSocket serverSocket;
    private Socket clientSocket;

    private static final int SERVER_PORT = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        chatBox = findViewById(R.id.chatBox);

        // Hiển thị IP của thiết bị
        String ipAddress = getLocalIpAddress();
        chatBox.setText("=== SERVER ===\nIP của bạn: " + ipAddress + "\nPort: " + SERVER_PORT + "\n\nĐang chờ Client kết nối...");

        new Thread(new ServerThread()).start();
    }

    // Lấy địa chỉ IP của thiết bị
    private String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Không tìm thấy IP";
    }

    class ServerThread implements Runnable {
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(SERVER_PORT);

                // Chờ client kết nối
                clientSocket = serverSocket.accept();

                handler.post(() -> {
                    chatBox.append("\n\n✓ Client đã kết nối!");
                    Toast.makeText(ServerActivity.this, "Client đã kết nối!", Toast.LENGTH_SHORT).show();
                });

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String message;
                while ((message = in.readLine()) != null) {
                    final String msg = message;
                    handler.post(() -> chatBox.append("\nClient: " + msg));

                    // Echo lại tin nhắn cho client (tùy chọn)
                    out.println("Server nhận: " + msg);
                }

            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> {
                    chatBox.append("\n\n✗ Lỗi: " + e.getMessage());
                    Toast.makeText(ServerActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
