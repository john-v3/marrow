package main

import (
	"context"
	"fmt"
	logger "john-v3/Logger"
	"log"
	"os"
	"time"

	"tinygo.org/x/bluetooth"
)

var adapter = bluetooth.DefaultAdapter

func main() {
	// Enable BLE interface.
	must("enable BLE stack", adapter.Enable())

	tracker := make(map[string]int16, 0)

	// Start scanning.
	println("scanning...")

	LogFile, err := os.Create("./log.txt")
	if err == nil {
		defer LogFile.Close()
	}

	if err != nil {
		log.Println(err)
	}

	test := logger.JcLogger{
		LogMode:   logger.Overwrite,
		LogObject: LogFile,
	}

	err = adapter.Scan(func(adapter *bluetooth.Adapter, device bluetooth.ScanResult) {
		key := fmt.Sprint(device.Address.String(), " ", device.LocalName(), " ", device.AdvertisementPayload)
		p := device.AdvertisementPayload

		tracker[key] = device.RSSI

		if len(p.ManufacturerData()) == 0 {
			return
		}

		// fmt.Println("Manufacturer data:", p.ManufacturerData()[0].CompanyID)

		if p.ManufacturerData()[0].CompanyID != 11 {
			return
		}

		message := ""
		tm := time.Now().UTC()
		message += fmt.Sprintln("----------------------------------")
		message += fmt.Sprintln(tm)
		message += fmt.Sprintln("----------------------------------")

		fmt.Println("Manufacturer data:", p.ManufacturerData())
		fmt.Println("Service data:", p.ServiceData())
		fmt.Println("Local name:", device.LocalName())
		// fmt.Println("raw:", device.ServiceData())
		fmt.Println("MAC:", device.Address.MAC)
		fmt.Println("Random?:", device.Address.IsRandom())
		fmt.Println("raw:", len(device.AdvertisementPayload.Bytes()))

		fmt.Println(message)
		test.Write(message)
		// time.Sleep(time.Second)

		// for k := range tracker {
		// 	delete(tracker, k)
		// }

	})

	must("start scan", err)
}

func StartAdvertising() {
	  	// Enable BLE interface.
	must("enable BLE stack", adapter.Enable())

	ctx, cancel := context.WithCancel(context.Background())
	adapter.SetConnectHandler(func(device bluetooth.Device, connected bool) {
		if connected {
			println("device connected:", device.Address.String())
			return
		}

		println("device disconnected:", device.Address.String())
		cancel()
	})

  	// Define the peripheral device info.
	adv := adapter.DefaultAdvertisement()
	must("config adv", adv.Configure(bluetooth.AdvertisementOptions{
		LocalName: "Go Bluetooth",
  	}))
  
  	// Start advertising
	must("start adv", adv.Start())
	
	// Stop advertising to release resources
	defer adv.Stop()

	println("advertising...")
	<- ctx.Done()
}

func SaveResult() {

}

func must(action string, err error) {
	if err != nil {
		panic("failed to " + action + ": " + err.Error())
	}
}
