package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.BaseItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DLC extends BaseItem {

    private Game game;

    public DLC(String title,BigDecimal price,LocalDateTime releaseDate,Game game){
        super(title, price, releaseDate);
        this.game=game;
    }
}
