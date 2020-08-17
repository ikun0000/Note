package main

import (
	"fmt"
)

func main() {
	var z []int
	fmt.Println(z, len(z), cap(z))
	if z == nil {
		fmt.Println("nil!")
	}

	// var a [10]int
	// fmt.Println(a, len(a), cap(a))

	// b := a[:5]
	// fmt.Println(b, len(b), cap(b))

	// c := a[5:]
	// fmt.Println(c, len(c), cap(c))
}
