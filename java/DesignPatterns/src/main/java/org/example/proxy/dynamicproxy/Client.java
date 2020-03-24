package org.example.proxy.dynamicproxy;

public class Client {

    public static void main(String[] args) {
        Host host = new Host();
        ProxyInvocationHandler proxyInvocationHandler = new ProxyInvocationHandler();
        proxyInvocationHandler.setRent(host);
        Rent rent = (Rent) proxyInvocationHandler.getProxy();

        rent.rent();
    }

}
