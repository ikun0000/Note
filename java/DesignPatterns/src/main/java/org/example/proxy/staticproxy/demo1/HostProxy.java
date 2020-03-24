package org.example.proxy.staticproxy.demo1;

public class HostProxy implements Rent {

    private Host host;

    public HostProxy(Host host) {
        this.host = host;
    }

    @Override
    public void rent() {
        see();
        host.rent();
        contract();
        fare();
    }

    public void see() {
        System.out.println("中介带你看房！");
    }

    public void contract() {
        System.out.println("签租赁合约！");
    }

    public void fare() {
        System.out.println("收取中介费！");
    }
}
