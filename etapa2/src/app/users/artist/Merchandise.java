package app.users.artist;

import lombok.Getter;

public class Merchandise {
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final int price;

    /**
     * Instantiates a new Merchandise.
     *
     * @param name        the name
     * @param description the description
     * @param price       the price
     */
    public Merchandise(final String name, final String description, final int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
