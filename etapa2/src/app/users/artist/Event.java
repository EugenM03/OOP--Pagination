package app.users.artist;

import lombok.Getter;

public final class Event {
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final String date;

    /**
     * Instantiates a new Event.
     *
     * @param name        the name
     * @param description the description
     * @param date        the date
     */
    public Event(final String name, final String description, final String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }
}
