package bg.sofia.uni.fmi.mjt.gameplatform.store.item;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BaseItem implements StoreItem{

    private String title;
    private BigDecimal price;
    private LocalDateTime releaseDate;

    private double rate;
    private int ratingCount=0;

    public BaseItem(String title,BigDecimal price,LocalDateTime releaseDate)
    {
        this.title=title;
        this.price=price;
        this.releaseDate=releaseDate;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public double getRating() {
        return rate/ratingCount;
    }

    @Override
    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    @Override
    public void setTitle(String title) {
        this.title= title;
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price=price;
    }

    @Override
    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate=releaseDate;
    }

    @Override
    public void rate(double rating) {
        rate+=rating;
        ratingCount++;
    }
}
