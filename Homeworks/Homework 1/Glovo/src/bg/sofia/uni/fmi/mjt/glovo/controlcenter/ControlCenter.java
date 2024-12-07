package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidLocationException;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidMapException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class ControlCenter implements ControlCenterApi {

    private static final Set<Character> MAP_ENTITY_TYPES = Set.of('.', '#', 'C', 'R', 'A', 'B');
    private MapEntity[][] mapLayout;
    private final int rows;
    private final int cols;

    public ControlCenter(char[][] mapLayout) {
        validateMapLayout(mapLayout);
        this.rows = mapLayout.length;
        this.cols = mapLayout[0].length;
        populateMapLayout(mapLayout);

    }

    @Override
    public DeliveryInfo findOptimalDeliveryGuy(Location restaurantLocation, Location clientLocation, double maxPrice,
                                               int maxTime, ShippingMethod shippingMethod) {
        validateLocation(restaurantLocation);
        validateLocation(clientLocation);
        List<DeliveryInfo> possibleDeliveries = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                MapEntity entity = mapLayout[i][j];

                if (entity.getType() == MapEntityType.DELIVERY_GUY_BIKE ||
                    entity.getType() == MapEntityType.DELIVERY_GUY_CAR) {

                    DeliveryInfo currentDeliveryInfo = isValidDelivery(entity, restaurantLocation,
                        clientLocation, maxPrice, maxTime);
                    if (currentDeliveryInfo != null) {
                        possibleDeliveries.add(currentDeliveryInfo);
                    }
                }
            }
        }
        return getMostOptimalDeliveryGuy(possibleDeliveries, shippingMethod);
    }

    private DeliveryInfo isValidDelivery(MapEntity entity, Location restaurantLocation, Location clientLocation,
                                         double maxPrice, int maxTime) {
        Location deliveryGuyLocation = entity.getLocation();
        DeliveryType deliveryType =
            entity.getType() == MapEntityType.DELIVERY_GUY_CAR ? DeliveryType.CAR : DeliveryType.BIKE;

        int distanceFromDriverToRestaurant =
            calculateShortestPath(entity.getLocation(), restaurantLocation);
        if (distanceFromDriverToRestaurant == -1) return null;

        int distanceFromRestaurantToClient = calculateShortestPath(restaurantLocation, clientLocation);
        if (distanceFromRestaurantToClient == -1) return null;

        int totalDistance = distanceFromDriverToRestaurant + distanceFromRestaurantToClient;
        double cost = (double) deliveryType.getPrice() * totalDistance;
        int time = deliveryType.getTime() * totalDistance;

        if ((maxPrice != -1 && cost > maxPrice) || (maxTime != -1 && time > maxTime)) {
            return null;
        }
        return new DeliveryInfo(deliveryGuyLocation, cost, time, deliveryType);
    }

    @Override
    public MapEntity[][] getLayout() {
        return this.mapLayout;
    }

    public void printLayout() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("%c ", mapLayout[i][j].getType().getSymbol());
            }
            System.out.println();
        }
    }

    private boolean isValidSymbol(char symbol) {
        return MAP_ENTITY_TYPES.contains(symbol);
    }

    private void validateMapLayout(char[][] mapLayout) {
        if (mapLayout == null) {
            throw new IllegalArgumentException("MapLayout is null");
        }
        int rows = mapLayout.length;
        if (rows == 0) {
            throw new InvalidMapException("Map must have at least 1 row");
        }
        int colSize = mapLayout[0].length;

        if (colSize == 0) {
            throw new InvalidMapException("Map must have at least one column");
        }
        for (int i = 0; i < rows; i++) {
            if (mapLayout[i] == null) {
                throw new InvalidMapException("Row " + i + " is null");
            }
            if (colSize != mapLayout[i].length) {
                throw new InvalidMapException("Invalid row sizes! All rows must have the same length ");
            }
            for (char ch : mapLayout[i]) {
                if (!isValidSymbol(ch)) {
                    throw new InvalidMapException("Invalid symbol: " + ch + "at row " + (i + 1));
                }
            }
        }

    }

    private MapEntityType getTypeFromSymbol(char symbol) {
        return switch (symbol) {
            case '.' -> MapEntityType.ROAD;
            case '#' -> MapEntityType.WALL;
            case 'C' -> MapEntityType.CLIENT;
            case 'R' -> MapEntityType.RESTAURANT;
            case 'A' -> MapEntityType.DELIVERY_GUY_CAR;
            case 'B' -> MapEntityType.DELIVERY_GUY_BIKE;
            default -> throw new IllegalArgumentException("Invalid Symbol In the Map" + symbol);
        };
    }

    private void populateMapLayout(char[][] mapLayout) {
        this.mapLayout = new MapEntity[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Location currLocation = new Location(i, j);
                MapEntityType currType = getTypeFromSymbol(mapLayout[i][j]);
                this.mapLayout[i][j] = new MapEntity(currLocation, currType);
            }
        }
    }

    private int calculateShortestPath(Location start, Location end) {
        final int moveDirections = 4;
        boolean[][] visited = new boolean[rows][cols];
        int[] rowDirections = {0, 1, 0, -1};
        int[] colDirections = {1, 0, -1, 0};
        Queue<Location> queue = new LinkedList<>();
        queue.add(start);
        visited[start.x()][start.y()] = true;
        int distance = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Location current = queue.poll();
                if (current.equals(end)) {
                    return distance;
                }
                for (int j = 0; j < moveDirections; j++) {
                    int newRow = current.x() + rowDirections[j];
                    int newCol = current.y() + colDirections[j];

                    if (isValidMove(newRow, newCol, visited)) {
                        visited[newRow][newCol] = true;
                        queue.add(new Location(newRow, newCol));
                    }
                }
            }
            distance++;
        }
        return -1;
    }

    private boolean isValidMove(int newRow, int newCol, boolean[][] visited) {
        return !(newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols ||
            mapLayout[newRow][newCol].getType() == MapEntityType.WALL || visited[newRow][newCol]);

    }

    private DeliveryInfo getMostOptimalDeliveryGuy(List<DeliveryInfo> deliveries, ShippingMethod shippingMethod) {
        if (shippingMethod == ShippingMethod.CHEAPEST) {
            return getCheapestDelivery(deliveries);
        } else {
            return getFastestDelivery(deliveries);
        }
    }

    private DeliveryInfo getFastestDelivery(List<DeliveryInfo> deliveries) {
        int size = deliveries.size();
        if (size == 0) return null;

        DeliveryInfo optimal = deliveries.get(0);
        for (int i = 1; i < size; i++) {
            DeliveryInfo current = deliveries.get(i);
            if (current.estimatedTime() < optimal.estimatedTime()) {
                optimal = current;
            }
        }
        return optimal;
    }

    private DeliveryInfo getCheapestDelivery(List<DeliveryInfo> deliveries) {
        int size = deliveries.size();
        if (size == 0) return null;

        DeliveryInfo optimal = deliveries.get(0);
        for (int i = 1; i < size; i++) {
            DeliveryInfo current = deliveries.get(i);
            if (current.price() < optimal.price()) {
                optimal = current;
            }
        }
        return optimal;
    }

    private void validateLocation(Location location) {
        if (location.x() < 0 || location.x() >= rows || location.y() < 0 || location.y() >= cols) {
            throw new InvalidLocationException("Location out of layout exception");
        }
    }

}
