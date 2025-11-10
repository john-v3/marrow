package logger

import (
	"fmt"
	"io"
	"os"
)

// for parallelization implementations
func GoFuncWriteln(a ...any) {
	st := fmt.Sprintln(a...)
	os.Stdout.WriteString(st)
}

type LogType int

const (
	Overwrite LogType = iota
	Append
)

var logTypeNames = map[LogType]string{
	Overwrite: "Overwrite",
	Append:    "Append",
}

func (lt LogType) String() string {
	return logTypeNames[lt]
}

type JcLogger struct {
	LogMode   LogType
	LogObject io.Writer
	LogClose io.Closer
}

func (jcl JcLogger) Write(input string) {
	jcl.LogObject.Write([]byte(input))
}

func (jcl JcLogger) Close() {
	jcl.LogClose.Close()
}
