package bg.sofia.uni.fmi.mjt.gameplatform.store.item;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface StoreItem {
    /**
     * Returns the title of the store item.
     *
     * @return the title of the store item
     */
    String getTitle();

    /**
     * Returns the price of the store item.
     *
     * @return the price of the store item
     */
    BigDecimal getPrice();

    /**
     * Returns the average rating of the store item.
     *
     * @return the average rating of the store item
     */
    double getRating();

    /**
     * Returns the release date of the store item.
     *
     * @return the release date of the store item
     */
    LocalDateTime getReleaseDate();

    /**
     * Sets the title of the store item.
     *
     * @param title the title of the store item
     */
    void setTitle(String title);

    /**
     * Sets the price of the store item.
     *
     * @param price the price of the store item
     */
    void setPrice(BigDecimal price);

    /**
     * Sets the release date of the store item.
     *
     * @param releaseDate the release date of the store item
     */
    void setReleaseDate(LocalDateTime releaseDate);

    /**
     * Rates the store item.
     *
     * @param rating the rating to be given for the store item
     */
    void rate(double rating);

}
