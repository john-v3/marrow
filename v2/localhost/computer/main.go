package main

import (
	"john-v3/marrowv2-primary/userinterface"
	"john-v3/marrowv2-primary/utilities"
)

func main() {
	netStr := utilities.GetOutBoundIP().String()
	userinterface.GenerateQRCodePopup(netStr)

	// start up the websocket
	utilities.InstantiateWebServer(netStr, "8080")

}
