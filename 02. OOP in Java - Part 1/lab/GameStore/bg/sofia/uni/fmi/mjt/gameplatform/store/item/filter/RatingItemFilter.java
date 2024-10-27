package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

public class RatingItemFilter implements ItemFilter{
    private double rating;
    public RatingItemFilter(double rating)
    {
        this.rating=rating;
    }

    @Override
    public boolean matches(StoreItem item) {
        return item.getRating() >=rating;
    }
}
