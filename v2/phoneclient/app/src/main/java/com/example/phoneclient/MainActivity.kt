package com.example.phoneclient

import WebSocketClient
import android.bluetooth.le.ScanSettings
import android.os.Bundle
import android.util.Log
import android.util.LogPrinter
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import io.github.g00fy2.quickie.config.BarcodeFormat

class MainActivity : ComponentActivity() {

    // private lateinit var scanQRCodeLauncher: ManagedActivityResultLauncher<Nothing?, QRResult>
    private var selectedBarcodeFormat = BarcodeFormat.FORMAT_ALL_FORMATS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhoneclientTheme {
                val test = remember { mutableStateOf("") }

                Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Greeting(test.value)
                        ButtonBeginScan(modifier = Modifier, resultAction = { input: String -> test.value = input  } )

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


     public fun ShowSnackbar(result: QRResult): String {
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
        return text
    }


    @Composable
    fun ButtonBeginScan(modifier : Modifier = Modifier, resultAction:(input: String) -> Unit) {

        val scanQRCodeLauncher = rememberLauncherForActivityResult(ScanQRCode()) {
            result: QRResult ->
                Log.e("RESULT", "Received value from QR Code Scan")
                resultAction(ShowSnackbar(result))
        }

        Button(onClick = { scanQRCodeLauncher.launch(null)}) {
            Text("Scan QR Code")
            Text("test")
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun ButtonBeginScanPreview() {
        PhoneclientTheme {
            ButtonBeginScan(modifier = Modifier, {})
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
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

@Preview(showBackground = true, name = "test")
@Composable
fun GreetingPreview() {
    PhoneclientTheme {
        Greeting("Android")
    }
}

