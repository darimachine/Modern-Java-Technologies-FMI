package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenter;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenterApi;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;

import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

public class Glovo implements GlovoApi {

    private final ControlCenterApi controlCenter;

    public Glovo(char[][] mapLayout) {
        this.controlCenter = new ControlCenter(mapLayout);
    }

    @Override
    public Delivery getCheapestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
        throws NoAvailableDeliveryGuyException {

        return getOptimalDelivery(client, restaurant, foodItem, -1, -1, ShippingMethod.CHEAPEST);
    }

    @Override
    public Delivery getFastestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
        throws NoAvailableDeliveryGuyException {
        return getOptimalDelivery(client, restaurant, foodItem, -1, -1, ShippingMethod.FASTEST);
    }

    @Override
    public Delivery getFastestDeliveryUnderPrice(MapEntity client, MapEntity restaurant, String foodItem,
                                                 double maxPrice) throws NoAvailableDeliveryGuyException {
        return getOptimalDelivery(client, restaurant, foodItem, -1, maxPrice, ShippingMethod.FASTEST);
    }

    @Override
    public Delivery getCheapestDeliveryWithinTimeLimit(MapEntity client, MapEntity restaurant, String foodItem,
                                                       int maxTime) throws NoAvailableDeliveryGuyException {
        return getOptimalDelivery(client, restaurant, foodItem, maxTime, -1, ShippingMethod.CHEAPEST);
    }

    private Delivery getOptimalDelivery(MapEntity client, MapEntity restaurant, String foodItem,
                                        int maxTime, double maxPrice, ShippingMethod shippingMethod) {
        validateParameters(client, restaurant, foodItem);

        Location restourantLocation = restaurant.getLocation();
        Location clientLocation = client.getLocation();
        DeliveryInfo deliveryInfo =
            controlCenter.findOptimalDeliveryGuy(restourantLocation, clientLocation, maxPrice, maxTime, shippingMethod);
        if (deliveryInfo == null) {
            throw new NoAvailableDeliveryGuyException("There is not available delivery guy");
        }
        Location deliveryGuyLocation = deliveryInfo.deliveryGuyLocation();
        double price = deliveryInfo.price();
        int estimatedTime = deliveryInfo.estimatedTime();

        return new Delivery(clientLocation, restourantLocation, deliveryGuyLocation, foodItem, price, estimatedTime);

    }

    private void validateNullParams(MapEntity client, MapEntity restaurant, String foodItem) {
        if (client == null) {
            throw new InvalidOrderException("Client is null");
        }
        if (restaurant == null) {
            throw new InvalidOrderException("Restaurant is null");
        }
        if (foodItem == null || foodItem.isBlank()) {
            throw new InvalidOrderException("foodItem is null or blank");
        }
    }

    private void validateParameters(MapEntity client, MapEntity restaurant, String foodItem) {
        validateNullParams(client, restaurant, foodItem);
        if (client.getType() != MapEntityType.CLIENT || restaurant.getType() != MapEntityType.RESTAURANT) {
            throw new InvalidOrderException("Client or Restaurant type is incorrect");
        }

        Location clientLocation = client.getLocation();
        Location restaurantLocation = restaurant.getLocation();

        validateLocationWithinMap(clientLocation, MapEntityType.CLIENT,
            "Invalid client location or no client in this location");

        validateLocationWithinMap(restaurantLocation, MapEntityType.RESTAURANT,
            "Invalid restaurant location or no restaurant in this location");

    }

    private void validateLocationWithinMap(Location location, MapEntityType expectedType,
                                           String errorMessage) {
        MapEntity[][] layout = controlCenter.getLayout();
        int rows = layout.length;
        int cols = layout[0].length;

        if (location.x() < 0 || location.x() >= rows || location.y() < 0 || location.y() >= cols ||
            layout[location.x()][location.y()].getType() != expectedType) {
            throw new InvalidOrderException(errorMessage);
        }
    }

}
