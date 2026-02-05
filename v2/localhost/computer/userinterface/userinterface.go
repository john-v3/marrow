package userinterface

import (
	"bytes"
	"fmt"
	"image"
	"log"

	"fyne.io/fyne/v2/app"
	"fyne.io/fyne/v2/canvas"
	"github.com/skip2/go-qrcode"
)

func GenerateQRCodePopup(text string) {
	target, _ := qrcode.Encode(text, qrcode.Highest, 512)

	tes, v1, err := image.Decode(bytes.NewReader(target))

	if err != nil {
		log.Fatalln(err)
	}

	fmt.Println("type: ", v1)

	a := app.New()
	w := a.NewWindow("test")

	image := canvas.NewImageFromImage(tes)
	image.FillMode = canvas.ImageFillOriginal

	w.SetContent(image)

	w.ShowAndRun()
}

