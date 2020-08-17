package main

import (
	"io"
	"os"
	"strings"
)

type rot13Reader struct {
	r io.Reader
}

// type Reader interface {
//     Read(p []byte) (n int, err error)
// }
func (r *rot13Reader) Read(p []byte) (n int, err error) {
	n, err = r.r.Read(p)
	return
}

func main() {
	s := strings.NewReader("Lbh penpxrq gur pbqr!")
	r := rot13Reader{s}
	io.Copy(os.Stdout, &r)
}
