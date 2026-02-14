package com.example.phoneclient

import WebSocketClient
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.phoneclient.ui.theme.PhoneclientTheme
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.QRResult.QRError
import io.github.g00fy2.quickie.QRResult.QRMissingPermission
import io.github.g00fy2.quickie.QRResult.QRSuccess
import io.github.g00fy2.quickie.QRResult.QRUserCanceled
import io.github.g00fy2.quickie.ScanQRCode
import java.net.URI


class MainActivity : ComponentActivity() {

    var webSocketClient: WebSocketClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhoneclientTheme {
                val receivedValue = remember { mutableStateOf("") }
                val parsedValue = remember { mutableStateOf<URI?>(null) }
                val webSocketLog = remember { mutableStateListOf(String()) }
                val messageHandler: (input: String?) -> Unit = {
                    input: String? ->
                        if (input != null)
                            webSocketLog.addLast(input)
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    InfoField(receivedValue.value)
                    ButtonBeginScan(
                        modifier = Modifier,
                        resultAction = { input: String ->
                            receivedValue.value = input
                            parsedValue.value = tryBuildingURI(input)
                        }
                    )

                    if (parsedValue.value != null) {
                        ButtonBeginWebSocketConnection(parsedValue) {
                            uRI ->
                            if (webSocketClient != null) {
                                webSocketClient?.send("init")
                                return@ButtonBeginWebSocketConnection
                            }

                            val result = beginWebSocketConnection(uRI, messageHandler)
                            if (result == null) return@ButtonBeginWebSocketConnection
                            webSocketClient = result

                            // webSocketClient.onWebsocketMessage(webSocketClient, )

                            webSocketLog.addFirst("connected")
                        }
                        ReceiveField(modifier = Modifier, webSocketLog)
                    }
                }
            }
        }
    }
}

fun tryBuildingURI(iPAddress: String): URI? {
    return URI.create("ws://$iPAddress:8080/echo")
}

fun beginWebSocketConnection(uRI: URI, onMessageOverride: (message: String?) -> Unit): WebSocketClient?
{
    val test = WebSocketClient(uRI, onMessageOverride)
    test.connectBlocking()

    return if (test.isOpen) test else null
}

@Composable
fun ButtonBeginScan(modifier : Modifier = Modifier,resultAction:(input: String) -> Unit) {

    val scanQRCodeLauncher = rememberLauncherForActivityResult(ScanQRCode()) {
            result: QRResult ->
        Log.e("RESULT", "Received value from QR Code Scan")
        resultAction(showSnackbar(result))
    }

    Button(onClick = { scanQRCodeLauncher.launch(null)}) {
        Text("Scan QR Code")
    }
}

@Composable
fun ButtonBeginWebSocketConnection(uri: MutableState<URI?>, resultAction: (input: URI) -> Unit) {
    if (uri.value != null) {
        Button(onClick = { resultAction(uri.value!!) }) {
            Text("Begin Connection")
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun ButtonBeginWebSocketConnectionPreviewNoValue() {
    PhoneclientTheme {
        ButtonBeginWebSocketConnection(mutableStateOf<URI?>(null)) {}
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun ButtonBeginWebSocketConnectionPreviewValue() {
    PhoneclientTheme {
        ButtonBeginWebSocketConnection(mutableStateOf<URI?>(URI.create("test"))) {}
    }
}


@Preview(showBackground = true)
@Composable
fun ButtonBeginScanPreview() {
    PhoneclientTheme {
        ButtonBeginScan(modifier = Modifier, {})
    }
}


fun showSnackbar(result: QRResult): String {

    val text = when (result) {
        is QRSuccess -> {
            result.content.rawValue
            // decoding with default UTF-8 charset when rawValue is null will not result in meaningful output, demo purpose
                ?: result.content.rawBytes?.let { String(it) }.orEmpty()
        }
        QRUserCanceled -> "User canceled"
        QRMissingPermission -> "Missing permission"
        is QRError -> "${result.exception.javaClass.simpleName}: ${result.exception.localizedMessage}"
    }

    return text
}

@Composable
fun InfoField(name: String, modifier: Modifier = Modifier) {
    if (name == "")
    {
        Text(
            text = "no ip detected",
            modifier = modifier
        )
    } else {
        Text(
            text = "payload: $name",
            modifier = modifier
        )
    }
}


// displays an array of string values
// the oldest one will disappear every 5 seconds
@Composable
fun ReceiveField(modifier: Modifier = Modifier, receivedInformation : List<String>) {

    var presenter = ""
    for (thing in receivedInformation) {
        presenter += thing + "\n"
    }

    Text(text = presenter, modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun ReceiveFieldLogPreview(modifier: Modifier = Modifier) {
    PhoneclientTheme {
        ReceiveField(modifier, listOf("test", "test2", "test3") )
    }
}

@Preview(showBackground = true, name = "Info with no Info")
@Composable
fun InfoPreviewNoInfo() {
    PhoneclientTheme {
        InfoField("")
    }
}


@Preview(showBackground = true, name = "Info with Info")
@Composable
fun InfoPreviewWithInfo() {
    PhoneclientTheme {
        InfoField(name = "192.168.0.1")
    }
}


