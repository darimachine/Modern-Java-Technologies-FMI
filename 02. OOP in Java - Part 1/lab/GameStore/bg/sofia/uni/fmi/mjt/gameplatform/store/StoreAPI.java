package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;

public interface StoreAPI {
    /**
     * Finds all store items that match the given filters.
     *
     * @param itemFilters the filters to be applied
     * @return an array of store items that match all filters or an empty array if no such items are found
     */
    StoreItem[] findItemByFilters(ItemFilter[] itemFilters);

    /**
     * Applies a promo code discount to all store items.
     * If the promo code is not valid, no discount is applied.
     *
     * @param promoCode the promo code to be applied
     */
    void applyDiscount(String promoCode);

    /**
     * Rates a store item.
     *
     * @param item the item to be rated
     * @param rating the rating to be given in the range [1, 5]
     * @return true if the item is successfully rated, false otherwise
     */
    boolean rateItem(StoreItem item, int rating);
}
