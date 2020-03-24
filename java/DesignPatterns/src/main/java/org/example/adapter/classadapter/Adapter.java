package org.example.adapter.classadapter;

public class Adapter extends NetLine implements NetToUsb {
    @Override
    public void handleRequest() {
        super.request();
    }
}
