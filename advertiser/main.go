package main

import (
	"context"
	"fmt"
	"os"
	"time"

	"tinygo.org/x/bluetooth"
)

var adapter = bluetooth.DefaultAdapter

func main() {
	must("enable BLE stack", adapter.Enable())

	ctx, cancel := context.WithCancel(context.Background())
	adapter.SetConnectHandler(func(device bluetooth.Device, connected bool) {
		GoFuncWriteln("test")
		if connected {
			println("device connected:", device.Address.String())
			return
		}

		println("device disconnected:", device.Address.String())
		cancel()
	})

	adv := adapter.DefaultAdvertisement()
	must("config adv", adv.Configure(bluetooth.AdvertisementOptions{
		LocalName: "Go Bluetooth",
	}))
	must("start adv", adv.Start())
	defer adv.Stop()

	println("advertising...")
	for {
		select {
		case <-time.After(1 * time.Second):
			address, _ := adapter.Address()
			println("Go Bluetooth /", address.MAC.String())
		case <-ctx.Done():
			return
		}
	}
}

func GoFuncWriteln(a ...any) {
	st := fmt.Sprintln(a...)
	os.Stdout.WriteString(st)
}

func must(action string, err error) {
	if err != nil {
		panic("failed to " + action + ": " + err.Error())
	}
}
