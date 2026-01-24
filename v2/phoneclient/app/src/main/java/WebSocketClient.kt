

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;


class WebSocketClient {
    val url = "wss://test.com/raw"
    val client = OkHttpClient()
    fun start() {
        val request = Request.Builder().url(url).build()


        val socket = client.newWebSocket(request, WebSocketListener())




    }

    // send data to web socket
    // receive data from web socket
}