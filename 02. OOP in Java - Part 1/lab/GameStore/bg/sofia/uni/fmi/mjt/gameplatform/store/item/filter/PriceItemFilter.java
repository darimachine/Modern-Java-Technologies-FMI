package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.math.BigDecimal;

public class PriceItemFilter implements ItemFilter{
    private BigDecimal lowerBound;
    private BigDecimal upperBound;

    public PriceItemFilter(BigDecimal lowerBound,BigDecimal upperBound)
    {
        this.lowerBound= lowerBound;
        this.upperBound= upperBound;
    }

    @Override
    public boolean matches(StoreItem item) {
        BigDecimal itemPrice = item.getPrice();
        if(itemPrice.compareTo(lowerBound)>=0 && itemPrice.compareTo(upperBound)<=0)
        {
            return true;
        }
        return false;

    }
}
