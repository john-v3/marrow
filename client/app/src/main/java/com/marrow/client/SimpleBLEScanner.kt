package com.marrow.client

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.content.Context
import androidx.annotation.RequiresPermission

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

        @SuppressLint("MissingPermission")
        StartScanning()
    }

    var test = object : ScanCallback() {

    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun StartScanning() {
        Scanner?.startScan(test)
    }



}