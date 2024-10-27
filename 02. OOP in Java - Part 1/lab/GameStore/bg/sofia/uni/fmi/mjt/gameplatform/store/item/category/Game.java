package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;


import bg.sofia.uni.fmi.mjt.gameplatform.store.item.BaseItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Game extends BaseItem {

    private final String genre;

    public Game(String title,BigDecimal price,LocalDateTime releaseDate,String genre){
        super(title, price, releaseDate);
        this.genre=genre;
    }
}
