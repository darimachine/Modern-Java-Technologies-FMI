package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;

import java.math.BigDecimal;
import java.util.Arrays;

public class GameStore implements StoreAPI{

    private StoreItem[] avaibleItems;
    boolean isDiscountApplied=false;
    public GameStore(StoreItem[] availableItems)
    {
        this.avaibleItems = Arrays.copyOf(availableItems,availableItems.length);
    }

    @Override
    public StoreItem[] findItemByFilters(ItemFilter[] itemFilters) {
        int filteredItemsSize=0;
        for(var item:avaibleItems)
        {
            boolean passesAllFilters=true;
            for(var filter:itemFilters)
            {
                if(!filter.matches(item))
                {
                    passesAllFilters=false;
                    break;
                }
            }
            if(passesAllFilters)
            {
                filteredItemsSize++;
            }
        }
        StoreItem[] filtered= new StoreItem[filteredItemsSize];
        int index=0;
        for(var item:avaibleItems) {
            boolean isAllFiltersPassed=true;
            for(var filter:itemFilters)
            {
                if(!filter.matches(item))
                {
                    isAllFiltersPassed=false;
                    break;
                }
            }
            if(isAllFiltersPassed)
            {
                filtered[index++] = item;
            }
        }

        return filtered;
    }
    private void makeDiscountByValue(BigDecimal discount)
    {
        for(var item:avaibleItems)
        {
            BigDecimal updatedPrice = item.getPrice().multiply(discount).setScale(2);
            item.setPrice(updatedPrice);
        }
    }
    @Override
    public void applyDiscount(String promoCode) {
        if(!(promoCode.equals("VAN40") || promoCode.equals("100YO"))){
            return;
        }
        if(!isDiscountApplied)
        {
            BigDecimal discount;
            if(promoCode.equals("VAN40"))
            {
                discount = BigDecimal.valueOf(0.60);
                makeDiscountByValue(discount);

            }
            else if(promoCode.equals("100YO"))
            {
                discount = BigDecimal.valueOf(0.00);
                makeDiscountByValue(discount);

            }
            isDiscountApplied=true;
        }
    }

    @Override
    public boolean rateItem(StoreItem item, int rating) {
        if(rating<1 || rating>5){
            return false;
        }
        item.rate(rating);
        return true;
    }
}
