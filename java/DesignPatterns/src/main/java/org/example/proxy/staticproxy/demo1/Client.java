package org.example.proxy.staticproxy.demo1;

public class Client {

    public static void main(String[] args) {
        Host host = new Host();
//        host.rent();
        HostProxy proxy = new HostProxy(host);
        proxy.rent();
    }

}
