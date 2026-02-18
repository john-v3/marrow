package utilities

import (
	"encoding/json"
	"fmt"
	"html/template"
	"log"
	"net/http"
	"sync"

	"github.com/gorilla/websocket"
)

// represents what a button is
type button struct {
	ID           string `json:"ID"`
	Name         string `json:"Name"`
	Category     string `json:"DataType"`
	CurrentValue string `json:"Value"`
}

// represents an action from the
// phone
type event struct {
	Name  string `json:"Name"`
	Value string `json:"Value"`
}

var upgrader = websocket.Upgrader{}

func handleInit() []byte {
	// just a default return object for proving

	payload := []button{
		{"1", "window 1", "button", "Super+1"},
		{"2", "window 2", "button", "Super+2"},
		{"3", "window 3", "button", "Super+3"},
		{"4", "window 4", "button", "Super+4"},
		{"5", "window 5", "button", "Super+5"},
		{"6", "window 6", "button", "Super+6"},
		{"7", "window 7", "button", "Super+7"},
		{"8", "window 8", "button", "Super+8"},
		{"9", "window 9", "button", "Super+9"},
	}

	byteLoad, _ := json.Marshal(payload)

	return byteLoad
}

func echo(w http.ResponseWriter, r *http.Request) {

	c, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		log.Print("upgrade:", err)
		return
	}

	defer c.Close()

	for {
		mt, message, err := c.ReadMessage()
		if err != nil {
			log.Println("read:", err)
			break
		}
		log.Printf("recv: %s", message)

		if string(message) == "init" {
			err = c.WriteMessage(mt, handleInit())
		} else {
			err = c.WriteMessage(mt, message)

			commander := x11CommandSuite{}

			var incoming event
			err := json.Unmarshal(message, &incoming)

			if err != nil {
				log.Println("could not interpret ", string(message))
			} else {
				commander.SendKeyPress(incoming.Value)
			}

		}

		if err != nil {
			log.Println("write:", err)
			break
		}
	}
}

func formHttpServer(BaseAddress string, Port string, wg *sync.WaitGroup) *http.Server {

	srv := &http.Server{Addr: BaseAddress + ":" + Port}
	http.HandleFunc("/", home)
	http.HandleFunc("/echo", echo)

	fmt.Println("Starting Server on " + BaseAddress + ":" + Port)
	go func() {
		defer wg.Done()

		if err := srv.ListenAndServe(); err != http.ErrServerClosed {
			log.Fatalf("ListenAndServe(): %v", err)
		}
	}()

	return srv
}

func InstantiateWebServer(BaseAddress string, Port string, httpServerExitDone *sync.WaitGroup) (*sync.WaitGroup, *http.Server) {

	httpServerExitDone.Add(1)
	server := formHttpServer(BaseAddress, Port, httpServerExitDone)

	// now close the server gracefully ("shutdown")
	// timeout could be given with a proper context
	// (in real world you shouldn't use TODO()).
	// if err := server.Shutdown(context.TODO()); err != nil {
	// 	panic(err) // failure/timeout shutting down the server gracefully
	// }

	// wait for goroutine started in startHttpServer() to stop
	// NOTE: as @sander points out in comments, this might be unnecessary.

	log.Println("server instantiated.")

	return httpServerExitDone, server
}

func home(w http.ResponseWriter, r *http.Request) {
	homeTemplate.Execute(w, "ws://"+r.Host+"echo")
}

var homeTemplate = template.Must(template.New("").Parse(`
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script>  
window.addEventListener("load", function(evt) {

    var output = document.getElementById("output");
    var input = document.getElementById("input");
    var ws;

    var print = function(message) {
        var d = document.createElement("div");
        d.textContent = message;
        output.appendChild(d);
        output.scroll(0, output.scrollHeight);
    };

    document.getElementById("open").onclick = function(evt) {
        if (ws) {
            return false;
        }
        ws = new WebSocket("{{.}}");
        ws.onopen = function(evt) {
            print("OPEN");
        }
        ws.onclose = function(evt) {
            print("CLOSE");
            ws = null;
        }
        ws.onmessage = function(evt) {
            print("RESPONSE: " + evt.data);
        }
        ws.onerror = function(evt) {
            print("ERROR: " + evt.data);
        }
        return false;
    };

    document.getElementById("send").onclick = function(evt) {
        if (!ws) {
            return false;
        }
        print("SEND: " + input.value);
        ws.send(input.value);
        return false;
    };

    document.getElementById("close").onclick = function(evt) {
        if (!ws) {
            return false;
        }
        ws.close();
        return false;
    };

});
</script>
</head>
<body>
<table>
<tr><td valign="top" width="50%">
<p>Click "Open" to create a connection to the server, 
"Send" to send a message to the server and "Close" to close the connection. 
You can change the message and send multiple times.
<p>
<form>
<button id="open">Open</button>
<button id="close">Close</button>
<p><input id="input" type="text" value="Hello world!">
<button id="send">Send</button>
</form>
</td><td valign="top" width="50%">
<div id="output" style="max-height: 70vh;overflow-y: scroll;"></div>
</td></tr></table>
</body>
</html>
`))
