package org.example.adapter.objectadapter;

public class TestDemo {

    public static void main(String[] args) {
        NetLine netLine = new NetLine();
        NetToUsb netToUsb = new Adapter(netLine);
        Computer computer = new Computer();

        computer.net(netToUsb);
    }

}
