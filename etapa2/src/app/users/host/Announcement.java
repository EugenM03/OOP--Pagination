package app.users.host;

import app.audio.LibraryEntry;
import lombok.Getter;

public final class Announcement extends LibraryEntry {
    @Getter
    private final String name;
    @Getter
    private final String description;

    /**
     * Instantiates a new Announcement.
     *
     * @param name        the name
     * @param description the description
     */
    public Announcement(final String name, final String description) {
        super(name);
        this.name = name;
        this.description = description;
    }
}
