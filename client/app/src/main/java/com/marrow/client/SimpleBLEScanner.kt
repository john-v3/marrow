package com.marrow.client

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.widget.Toast
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat

class SimpleBLEScanner (private var context: Context, private var activity1: MainActivity)
{
    private var Scanner : BluetoothLeScanner? = null
    private var BluetoothManager : BluetoothManager? = null
    private var BluetoothAdapter : BluetoothAdapter? = null

    init {
        BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?
        // if null panic
        BluetoothAdapter = BluetoothManager?.adapter
        // if null panic
        Scanner = BluetoothAdapter?.bluetoothLeScanner
        // if null panic
    }

    var test = object : ScanCallback() {
        override fun onBatchScanResults(results: List<ScanResult?>?) {
            super.onBatchScanResults(results)
            Log.w("BluetoothBLEScanning", "batch result")
            Log.w("BluetoothBLEScanning", results?.size.toString() + " " + results.toString())
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.w("BluetoothBLEScanning", "Error: $errorCode")
        }

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            Log.w("BluetoothBLEScanning", "scan result received ${result?.scanRecord?.deviceName}")
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION])
    fun StartScanning() {
        if (ActivityCompat.checkSelfPermission(
                this.context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission.launch(Manifest.permission.BLUETOOTH_SCAN)
        }

        if (ActivityCompat.checkSelfPermission(
                this.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        Scanner?.startScan(test)
    }


    private val requestPermission =
        this.activity1.registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted ->

            if (isGranted) {
                // Permission granted — safe to use Bluetooth
                if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.BLUETOOTH_CONNECT)
                    == PackageManager.PERMISSION_GRANTED) {
                    // Permission not granted — handle or request
                    if (ActivityCompat.checkSelfPermission(
                            this.context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {

                    }

                }
            } else {
                // Permission denied — show rationale or disable functionality
                Toast.makeText(this.context, "permission denied",
                    Toast.LENGTH_SHORT).show()
            }
        }
}