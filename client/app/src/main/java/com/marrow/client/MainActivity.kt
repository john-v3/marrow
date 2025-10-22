package com.marrow.client

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.marrow.client.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        checkAndRequestBluetoothPermission()
    }



    private val requestBluetoothConnectPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted ->

            if (isGranted) {
                // Permission granted — safe to use Bluetooth
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                    == PackageManager.PERMISSION_GRANTED

                    ) {
                    // Permission not granted — handle or request
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                    }
                    enableBluetooth()
                }
            } else {
                // Permission denied — show rationale or disable functionality
                Toast.makeText(this, "Bluetooth permission denied",
                    Toast.LENGTH_SHORT).show()
            }
        }

    private fun checkAndRequestBluetoothPermission() {

        // check if permission is granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted — handle or request
            enableBluetooth()
        } else {
            // 3. Request the permission
            requestBluetoothConnectPermission.launch(Manifest.permission.BLUETOOTH_CONNECT)
        }
    }

    private fun enableBluetooth() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted — handle or request
            Toast.makeText(this, "Bluetooth permission not granted", Toast.LENGTH_SHORT).show()
            return
        }
        val bluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivity(intent)
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        Button(onClick = {TestOut()},
            enabled = true,
            content = {ButtonType1()},
        )

        for (t in v1Arr) {
            Button(onClick = {TestOut()},
                enabled = true,
                content = {ButtonType1()},
            )
        }
    }
}

@Composable
fun ButtonType1() {
    Text(
        text = "test"
    )
}

var v1Arr = arrayOf("test", "test3")

fun TestOut() {

}

@Preview(showBackground = true, name = "test")
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}

enum class CommandBehavior {NORMAL, TOGGLE}

public class Command {
    var incomingCommandBehavior : String = ""
    var name : String = "default"
    var index : Int = 0

    @Composable
    fun GenerateButton() {
        Button(onClick = {TestOut()},
            enabled = true,
            content = {ButtonType1()},
        )
    }

    fun SendSignal(command : String) {
        // bluetooth stuff
    }
}
