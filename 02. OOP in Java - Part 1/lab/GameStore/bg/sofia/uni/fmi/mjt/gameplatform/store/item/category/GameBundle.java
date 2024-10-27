package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.BaseItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

public class GameBundle extends BaseItem {
    private Game[] games;

    public GameBundle(String title,BigDecimal price,LocalDateTime releaseDate, Game[] games)
    {
        super(title, price, releaseDate);
        //this.games= Arrays.copyOf(games,games.length);
        this.games= games;
    }

}
