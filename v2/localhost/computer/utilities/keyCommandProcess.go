package utilities

import (
	"runtime"
	"time"

	"github.com/micmonay/keybd_event"
)

type WindowSwitcher struct {
	window1  keybd_event.KeyBonding
	window2  keybd_event.KeyBonding
	window3  keybd_event.KeyBonding
	window4  keybd_event.KeyBonding
	window5  keybd_event.KeyBonding
	window6  keybd_event.KeyBonding
	window7  keybd_event.KeyBonding
	window8  keybd_event.KeyBonding
	window9  keybd_event.KeyBonding
	window10 keybd_event.KeyBonding
}

func (A *WindowSwitcher) ProcessCommand(test string) {

	switch test {
	case "window1":
		A.window1.Launching()
	case "window2":
		A.window2.Launching()
	case "window3":
		A.window3.Launching()
	case "window4":
		A.window4.Launching()
	case "window5":
		A.window5.Launching()
	case "window6":
		A.window6.Launching()
	case "window7":
		A.window7.Launching()
	case "window8":
		A.window8.Launching()
	case "window9":
		A.window9.Launching()
	case "window0":
		A.window10.Launching()
	}

}

func NewWindowSwitcher() WindowSwitcher {

	window1, err := keybd_event.NewKeyBonding()
	if err != nil {
		panic(err)
	}
	window1.SetKeys(keybd_event.VK_1)
	window1.HasSuper(true)
	window2, err := keybd_event.NewKeyBonding()
	if err != nil {
		panic(err)
	}
	window2.SetKeys(keybd_event.VK_2)
	window2.HasSuper(true)
	window3, err := keybd_event.NewKeyBonding()
	if err != nil {
		panic(err)
	}
	window3.SetKeys(keybd_event.VK_3)
	window3.HasSuper(true)
	window4, err := keybd_event.NewKeyBonding()
	if err != nil {
		panic(err)
	}
	window4.SetKeys(keybd_event.VK_4)
	window4.HasSuper(true)
	window5, err := keybd_event.NewKeyBonding()
	if err != nil {
		panic(err)
	}
	window5.SetKeys(keybd_event.VK_5)
	window5.HasSuper(true)
	window6, err := keybd_event.NewKeyBonding()
	if err != nil {
		panic(err)
	}
	window6.SetKeys(keybd_event.VK_6)
	window6.HasSuper(true)
	window7, err := keybd_event.NewKeyBonding()
	if err != nil {
		panic(err)
	}
	window7.SetKeys(keybd_event.VK_7)
	window7.HasSuper(true)
	window8, err := keybd_event.NewKeyBonding()
	if err != nil {
		panic(err)
	}
	window8.SetKeys(keybd_event.VK_8)
	window8.HasSuper(true)
	window9, err := keybd_event.NewKeyBonding()
	if err != nil {
		panic(err)
	}
	window9.SetKeys(keybd_event.VK_9)
	window9.HasSuper(true)
	window0, err := keybd_event.NewKeyBonding()
	if err != nil {
		panic(err)
	}
	window0.SetKeys(keybd_event.VK_0)
	window0.HasSuper(true)

	payload := WindowSwitcher{
		window1:  window1,
		window2:  window2,
		window3:  window3,
		window4:  window4,
		window5:  window5,
		window6:  window6,
		window7:  window7,
		window8:  window8,
		window9:  window9,
		window10: window0,
	}

	// For linux, it is very important to wait 2 seconds
	if runtime.GOOS == "linux" {
		time.Sleep(2 * time.Second)
	}

	return payload
}
