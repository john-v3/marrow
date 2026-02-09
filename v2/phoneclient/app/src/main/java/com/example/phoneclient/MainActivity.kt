package com.example.phoneclient

import WebSocketClient
import android.bluetooth.le.ScanSettings
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.phoneclient.ui.theme.PhoneclientTheme
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.QRResult.QRError
import io.github.g00fy2.quickie.QRResult.QRMissingPermission
import io.github.g00fy2.quickie.QRResult.QRSuccess
import io.github.g00fy2.quickie.QRResult.QRUserCanceled
import io.github.g00fy2.quickie.ScanCustomCode
import io.github.g00fy2.quickie.ScanQRCode
import io.github.g00fy2.quickie.config.BarcodeFormat
import io.github.g00fy2.quickie.content.QRContent
import java.net.URI


class MainActivity : ComponentActivity() {

    private var selectedBarcodeFormat = BarcodeFormat.FORMAT_ALL_FORMATS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            PhoneclientTheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        val uri = URI.create("ws://10.249.150.125:8080/echo")
        val test = WebSocketClient(uri)
        test.connect()

        Thread.sleep(1000)
        if (test.isOpen) {
            test.send("test")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true, name = "test")
@Composable
fun GreetingPreview() {
    PhoneclientTheme {
        Greeting("Android")
    }
}

@Composable
fun ButtonBeginScan(modifier : Modifier = Modifier) {
    val scanQRCodeLauncher = rememberLauncherForActivityResult(ScanQRCode()) {
        result -> {
         qRResult ->
        }
    }

    Button(onClick = { scanQRCodeLauncher.launch(null)}) {
        Text("Scan QR Code")
    }
}


fun
