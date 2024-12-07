package bg.sofia.uni.fmi.mjt.glovo.delivery;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;

public record DeliveryInfo(Location deliveryGuyLocation, double price,
                           int estimatedTime, DeliveryType deliveryType) {

}

//public class DeliveryInfo {
//
//    private Location deliveryGuyLocation;
//    private double price;
//    private int estimatedTime;
//    private DeliveryType deliveryType;
//
//    public DeliveryInfo(Location deliveryGuyLocation, double price,
//                        int estimatedTime, DeliveryType deliveryType) {
//        this.deliveryGuyLocation = deliveryGuyLocation;
//        this.price = price;
//        this.estimatedTime = estimatedTime;
//        this.deliveryType = deliveryType;
//    }
//
//    public Location getDeliveryGuyLocation() {
//        return deliveryGuyLocation;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public int getEstimatedTime() {
//        return estimatedTime;
//    }
//
//    public DeliveryType getDeliveryType() {
//        return deliveryType;
//    }
//}
