package app.user;

import lombok.Getter;

public final class Announcement {
    @Getter
    private final String name;
    @Getter
    private final String description;

    public Announcement(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
