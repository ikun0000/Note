package org.example.abstractfactory;

public class HuaweiProductFactory implements ProductFactory {
    @Override
    public Phone getPhone() {
        return new HuaweiPhone();
    }

    @Override
    public Router getRouter() {
        return new HuaweiRouter();
    }
}
