package org.example.abstractfactory;

public class HuaweiRouter implements Router {
    @Override
    public void openWifi() {
        System.out.println("华为路由器开启WIFI");
    }

    @Override
    public void setting() {
        System.out.println("华为路由器设置");
    }
}
