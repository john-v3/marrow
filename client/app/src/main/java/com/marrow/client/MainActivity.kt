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
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

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

        val test = SimpleGattServerAdvertising(this.applicationContext, this)
        test.Start()

        val test2 = SimpleBLEScanner(this.applicationContext, this)
        test2.StartScanning()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    val test = GetSamplePayload()

    Column {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        Text(
            text = "Hello $name!",
            modifier = modifier
        )

        for (t in test) {
            t.GenerateButton()
        }
    }
}

@Composable
fun ButtonType1() {
    Text(
        text = "test"
    )
}

@Preview(showBackground = true, name = "test")
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}

private fun GetSamplePayload(): Array<Command> {
    val payload = arrayOf(
        Command(0, "test", 0),
        Command(0, "test2", 1),
    )

    return payload
}

enum class CommandTypes(val value: Int) {
    PRESS(0),
    TOGGLE(1);

    companion object {
        fun fromInt(value: Int) = CommandTypes.entries.first { it.value == value }
    }
}

public class Command (incomingBehavior: Int = 0,
                      val name: String = "default",
                      var index: Int = 0)
{
    var incomingCommandBehavior : CommandTypes = CommandTypes.fromInt(incomingBehavior)

    @Composable
    fun GenerateButton() {
        Button(onClick = {this.SendSignal(index)},
            enabled = true,
            content = {Text( text = name)},
        )
    }

    // There should be
    fun SendSignal(command : Int) {
        // perform any effects,
        // there should be something
    }
}



