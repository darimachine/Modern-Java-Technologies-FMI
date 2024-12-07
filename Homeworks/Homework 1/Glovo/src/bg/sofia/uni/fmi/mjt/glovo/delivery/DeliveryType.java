package bg.sofia.uni.fmi.mjt.glovo.delivery;

public enum DeliveryType {
    CAR(5, 3),
    BIKE(3, 5);

    private final int price;
    private final int time;

    DeliveryType(int price, int time) {
        this.price = price;
        this.time = time;
    }

    public int getPrice() {
        return price;
    }

    public int getTime() {
        return time;
    }
}
