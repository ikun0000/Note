package org.example.adapter.classadapter;

public class TestDemo {

    public static void main(String[] args) {
        NetLine netLine = new NetLine();
        NetToUsb netToUsb = new Adapter();
        Computer computer = new Computer();

        computer.net(netToUsb);
    }

}
