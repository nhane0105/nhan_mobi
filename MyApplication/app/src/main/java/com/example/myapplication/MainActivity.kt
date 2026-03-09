package com.example.myapplication

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                NetworkCheckScreen()
            }
        }
    }
}

@Composable
fun NetworkCheckScreen() {
    val context = LocalContext.current

    var statusText by remember { mutableStateOf("Đang kiểm tra...") }

    // chạy kiểm tra 1 lần khi mở app
    LaunchedEffect(Unit) {
        if (isNetworkAvailable(context)) {
            val ok = checkInternet("https://www.google.com")
            statusText = if (ok) {
                "✅ Kết nối Internet thành công (Google OK)"
            } else {
                "⚠️ Có mạng nhưng không truy cập được Google"
            }
        } else {
            statusText = "❌ Không có kết nối mạng"
        }
    }

    // UI
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = statusText)
    }
}

// Kiểm tra có mạng không (WiFi / Mobile Data)
fun isNetworkAvailable(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = cm.activeNetwork ?: return false
    val capabilities = cm.getNetworkCapabilities(network) ?: return false

    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
}

// Check Internet bằng cách kết nối Google
suspend fun checkInternet(urlString: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val urlConnection = URL(urlString).openConnection() as HttpURLConnection
            urlConnection.requestMethod = "HEAD"
            urlConnection.connectTimeout = 3000
            urlConnection.readTimeout = 3000
            urlConnection.connect()

            val responseCode = urlConnection.responseCode
            responseCode == 200
        } catch (e: Exception) {
            false
        }
    }
}
