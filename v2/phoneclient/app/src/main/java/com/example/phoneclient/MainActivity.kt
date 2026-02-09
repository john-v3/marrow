package com.example.phoneclient

import WebSocketClient
import android.bluetooth.le.ScanSettings
import android.os.Bundle
import android.util.Log
import android.util.LogPrinter
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import java.util.logging.Logger


class MainActivity : ComponentActivity() {

    private var selectedBarcodeFormat = BarcodeFormat.FORMAT_ALL_FORMATS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.w("phoneclient", "test test 123")
        enableEdgeToEdge()
        setContent {
            PhoneclientTheme {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Greeting("test")
                        ButtonBeginScan()
                    }
            }
        }

//        val uri = URI.create("ws://10.249.150.125:8080/echo")
//        val test = WebSocketClient(uri)
//        test.connect()
//
//        Thread.sleep(1000)
//        if (test.isOpen) {
//            test.send("test")
//        }
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

    val scanQRCodeLauncher = rememberLauncherForActivityResult(ScanQRCode()) { result ->
        {
            showSnackbar(result)
        }
    }

    Button(onClick = { scanQRCodeLauncher.launch(null)}) {
        Text("Scan QR Code")
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonBeginScanPreview() {
    PhoneclientTheme {
        ButtonBeginScan()
    }
}

private fun showSnackbar(result: QRResult) {
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

    Log.w("QRCodeResult", text)
    Log.w("QRCodeResult", "test test 123")
}
