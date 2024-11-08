package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

public enum FuelType {
    DIESEL(3),
    PETROL(3),
    HYBRID(1),
    ELECTRICITY(0),
    HYDROGEN(0);

    private final int cost;
    FuelType(int cost)
    {
        this.cost=cost;
    }
    public int getCost()
    {
        return cost;
    }
}
