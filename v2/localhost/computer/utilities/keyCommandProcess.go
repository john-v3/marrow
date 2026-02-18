package utilities

import (
	"log"
	"os/exec"
)

type KeyInjector interface {
	SendKeyPress(combo string) error
}

type x11CommandSuite struct { }

type windowsCommandSuite struct {

}

func (flavor x11CommandSuite) SendKeyPress(t1 string) error {
	err := exec.Command("xdotool", "key", t1).Run()
	return err
}

func (flavor windowsCommandSuite) SendKeyPress(t1 string) error {
	log.Fatal("not implemented")
	return nil
}

