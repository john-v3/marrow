package com.marrow.client

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context

class SimpleBLEScanner
    (private val context: Context, private val activity1: MainActivity) {

        private val Scanner : BluetoothLeScanner = BluetoothAdapter.getBluetoothLeScanner()

    fun StartScanning() {

    }

}