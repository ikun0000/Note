package org.example.factorypattern.simple;

public class CarFactory {

    public static Car getCar(String carName) {
        if (carName.equals("大众")) {
            return new DazhongCar();
        } else if (carName.equals("特斯拉")) {
            return new TeslCar();
        } else {
            return null;
        }
    }

    public static Car getTesil() {
        return new TeslCar();
    }

    public static Car getDazhong() {
        return new DazhongCar();
    }

}
