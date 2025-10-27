package com.marrow.client

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.content.pm.PackageManager
import android.os.ParcelUuid
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import java.util.UUID

class SimpleGattServerActivity : ComponentActivity() {
    private var bluetoothManager: BluetoothManager? = null
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var gattServer: BluetoothGattServer? = null
    private var advertiser: BluetoothLeAdvertiser? = null

    private val serviceUUID = UUID.fromString("0000180D-0000-1000-8000-00805F9B34FB") // Heart Rate
    private val charUUID = UUID.fromString("00002A37-0000-1000-8000-00805F9B34FB")

    // Handle client interactions
    private val gattServerCallback = object : BluetoothGattServerCallback() {
        override fun onConnectionStateChange(device: BluetoothDevice, status: Int, newState: Int) {
            super.onConnectionStateChange(device, status, newState)
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> println("Device connected: ${device.address}")
                BluetoothProfile.STATE_DISCONNECTED -> println("Device disconnected: ${device.address}")
            }
        }

        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onCharacteristicReadRequest(
            device: BluetoothDevice,
            requestId: Int,
            offset: Int,
            characteristic: BluetoothGattCharacteristic
        ) {
            println("Read request from ${device.address}")
            gattServer?.sendResponse(
                device, requestId, BluetoothGatt.GATT_SUCCESS, offset,
                byteArrayOf(0x01, 0x02)
            )
        }

        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onCharacteristicWriteRequest(
            device: BluetoothDevice,
            requestId: Int,
            characteristic: BluetoothGattCharacteristic,
            preparedWrite: Boolean,
            responseNeeded: Boolean,
            offset: Int,
            value: ByteArray
        ) {
            println("Write request: ${value.joinToString()}")
            if (responseNeeded) {
                gattServer?.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, null)
            }
        }
    }

    // Ask for runtime Bluetooth permissions
    @SuppressLint("MissingPermission")
    private val requestBluetoothPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if (result.all { it.value }) startGattServer()
        else println("Bluetooth permissions denied.")
    }

    @RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
    override fun onStart() {
        super.onStart()
        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager?.adapter
        advertiser = bluetoothAdapter?.bluetoothLeAdvertiser

        requestBluetoothPermission.launch(
            arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_ADVERTISE
            )
        )
    }

    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_CONNECT])
    private fun startGattServer() {
        gattServer = bluetoothManager?.openGattServer(this, gattServerCallback)

        val service = BluetoothGattService(
            serviceUUID,
            BluetoothGattService.SERVICE_TYPE_PRIMARY
        )

        val characteristic = BluetoothGattCharacteristic(
            charUUID,
            BluetoothGattCharacteristic.PROPERTY_READ or BluetoothGattCharacteristic.PROPERTY_WRITE,
            BluetoothGattCharacteristic.PERMISSION_READ or BluetoothGattCharacteristic.PERMISSION_WRITE
        )

        service.addCharacteristic(characteristic)
        gattServer?.addService(service)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_ADVERTISE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        startAdvertising()
    }

    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_ADVERTISE])
    private fun startAdvertising() {
        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setConnectable(true)
            .setTimeout(0)
            .build()

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .addServiceUuid(ParcelUuid(serviceUUID))
            .build()

        advertiser?.startAdvertising(settings, data, object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                println("Advertising started successfully.")
            }

            override fun onStartFailure(errorCode: Int) {
                println("Advertising failed: $errorCode")
            }
        })
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADVERTISE)
    override fun onDestroy() {
        super.onDestroy()
        advertiser?.stopAdvertising(object : AdvertiseCallback() {})

        // check permissions
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        gattServer?.close()
    }
}