package main

import (
	"context"
	"john-v3/marrowv2-primary/userinterface"
	"john-v3/marrowv2-primary/utilities"
	"os"
	"os/signal"
	"sync"
	"syscall"
)

func main() {

	waiter := &sync.WaitGroup{}
	netStr := utilities.GetOutBoundIP().String()

	// start up the websocket
	_, server := utilities.InstantiateWebServer(netStr, "8080", waiter)

	// show the popup
	userinterface.GenerateQRCodePopup(netStr)

	// Listen for incoming OS signals.
	signalChan := make(chan os.Signal, 1)
	signal.Notify(signalChan, syscall.SIGINT, syscall.SIGTERM)

	<-signalChan
	server.Shutdown(context.TODO())

}

