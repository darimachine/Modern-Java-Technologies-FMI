package bg.sofia.uni.fmi.mjt.newsfeed.enums;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.InvalidCategoryException;

public enum Category implements ValuedEnumAPI {
    BUSINESS("business"),
    ENTERTAINMENT("entertainment"),
    GENERAL("general"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
    TECHNOLOGY("technology");
    private final String value;

    Category(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static Category fromString(String category) {
        for (Category c : Category.values()) {
            if (c.getValue().equalsIgnoreCase(category)) {
                return c;
            }
        }
        throw new InvalidCategoryException("Invalid category: " + category);
    }
}
