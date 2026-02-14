import android.util.Log
import org.java_websocket.WebSocket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.URI

class WebSocketClient(address: URI, private val onMessageOverride: (message: String?) -> Unit) : WebSocketClient(address) {

    override fun onOpen(handshakedata: ServerHandshake?) {
        println("Connected to server")
    }

    override fun onMessage(message: String?) {
        println("Message from server: $message")
        this.onMessageOverride(message)
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        println("Disconnected from server: $reason")
    }

    override fun onError(ex: Exception?) {
        ex?.printStackTrace()
    }

}


