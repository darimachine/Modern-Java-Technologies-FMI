package bg.sofia.uni.fmi.mjt.newsfeed.enums;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.InvalidCategoryException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CategoryTest {

    @Test
    void testFromStringValidCategory() {
        assertEquals(Category.BUSINESS, Category.fromString("business"));
        assertEquals(Category.ENTERTAINMENT, Category.fromString("entertainment"));
        assertEquals(Category.GENERAL, Category.fromString("general"));
        assertEquals(Category.HEALTH, Category.fromString("health"));
        assertEquals(Category.SCIENCE, Category.fromString("science"));
        assertEquals(Category.SPORTS, Category.fromString("sports"));
        assertEquals(Category.TECHNOLOGY, Category.fromString("technology"));
    }
    @Test
    void testFromStringValidCategoryCaseInsensitive() {

        assertEquals(Category.BUSINESS, Category.fromString("BUSINESS"));
        assertEquals(Category.SPORTS, Category.fromString("sPorTs"));
        assertEquals(Category.TECHNOLOGY, Category.fromString("technology"));
    }
    @Test
    void testFromStringInvalidCategory() {

        assertThrows(InvalidCategoryException.class, () -> Category.fromString("invalid-category"),
            "Invalid category should throw an exception");

    }
}
