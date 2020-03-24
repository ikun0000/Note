package org.example.adapter.objectadapter;

public class Adapter implements NetToUsb {

    private NetLine netLine;

    public Adapter(NetLine netLine) {
        this.netLine = netLine;
    }

    @Override
    public void handleRequest() {
        netLine.request();
    }
}
