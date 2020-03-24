package org.example.abstractfactory;

public class XiaomiProductFactory implements ProductFactory {
    @Override
    public Phone getPhone() {
        return new XiaomiPhone();
    }

    @Override
    public Router getRouter() {
        return new XiaomiRouter();
    }
}
