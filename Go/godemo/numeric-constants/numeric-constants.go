package main

import (
	"fmt"
)

const (
	Big   = 1 << 100
	Small = Big >> 99
)

func neadInt(x int) int {
	return x*10 + 1
}

func neadFloat(x float64) float64 {
	return x * 0.1
}

func main() {
	fmt.Println(neadInt(Small))
	fmt.Println(neadFloat(Small))
	fmt.Println(neadFloat(Big))
}
