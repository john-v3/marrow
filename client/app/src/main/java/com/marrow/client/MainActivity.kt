package com.marrow.client

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.marrow.client.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        // connect to server
        //val gatt = SimpleGattServerActivity()
        //gatt.onCreate()
        // main content
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

        var test = SimpleGattServerActivity(this.applicationContext, this)
        test.Start()
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
