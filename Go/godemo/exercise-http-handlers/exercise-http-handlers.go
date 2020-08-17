package main

import (
	"fmt"
	"log"
	"net/http"
)

type String string

type Struct struct {
	Greeting string
	Punct    string
	Who      string
}

func (s String) ServeHTTP(
	w http.ResponseWriter,
	r *http.Request) {
	fmt.Fprintf(w, string(s))
}

func (s *Struct) ServeHTTP(
	w http.ResponseWriter,
	r *http.Request) {
	str := fmt.Sprintf("Greeting = %s, Punct = %s, Who = %s",
		s.Greeting, s.Punct, s.Who)
	fmt.Fprintf(w, str)
}

func main() {
	// your http.Handle calls here
	http.Handle("/string", String("I'm a frayed knot."))
	http.Handle("/struct", &Struct{"Hello", ":", "Gophers!"})
	log.Fatal(http.ListenAndServe("localhost:4000", nil))
}
