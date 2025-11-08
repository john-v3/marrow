package com.marrow.client

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity

class SimpleBluetoothPermissionHandler constructor(private val appContext: Context) : ComponentActivity() {

    fun CheckAndRequestBluetoothPermission() {

        // check if permission is granted
        if (ContextCompat.checkSelfPermission(
                this.appContext, // must be an activity context
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
        if (ActivityCompat.checkSelfPermission(this.appContext, Manifest.permission.BLUETOOTH_CONNECT)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted — handle or request
            Toast.makeText(this, "Bluetooth permission not granted", Toast.LENGTH_SHORT).show()
            return
        }

        // start the bluetooth service
        val bluetoothManager = appContext.getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivity(intent)
        }
    }

    private val requestBluetoothConnectPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted ->

            if (isGranted) {
                // Permission granted — safe to use Bluetooth
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                    == PackageManager.PERMISSION_GRANTED) {
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


}

