package main

import (
	"fmt"

	"tinygo.org/x/bluetooth"
)

var adapter = bluetooth.DefaultAdapter

func main() {
	// Enable BLE interface.
	must("enable BLE stack", adapter.Enable())

	tracker := make(map[string]int16, 0)

	// Start scanning.
	println("scanning...")
	err := adapter.Scan(func(adapter *bluetooth.Adapter, device bluetooth.ScanResult) {

		
		key := fmt.Sprint(device.Address.String(), " ", device.LocalName())
		tracker[key] = device.RSSI

		fmt.Println("----------------------------------")
		for i, n := range tracker {
			fmt.Println(n, " ", i)
		}
		fmt.Println("----------------------------------")

	})

	// if bluetooth device is not enabled
	must("start scan", err)
}

func must(action string, err error) {
	if err != nil {
		panic("failed to " + action + ": " + err.Error())
	}
}

