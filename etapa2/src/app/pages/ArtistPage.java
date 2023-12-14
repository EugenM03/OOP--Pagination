package app.pages;

import app.audio.Collections.Album;
import app.users.artist.Artist;
import app.users.artist.Event;
import app.users.artist.Merchandise;
import lombok.Getter;

import java.util.ArrayList;

public final class ArtistPage extends Page {
    @Getter
    private final ArrayList<Album> albums;
    @Getter
    private final ArrayList<Merchandise> merchandise;
    @Getter
    private final ArrayList<Event> events;
    @Getter
    private Artist pageOwner;

    public ArtistPage() {
        albums = new ArrayList<>();
        merchandise = new ArrayList<>();
        events = new ArrayList<>();
    }

    public ArtistPage(final Artist pageOwner, final ArrayList<Album> albums,
                      final ArrayList<Merchandise> merchandise, final ArrayList<Event> events) {
        this.pageOwner = pageOwner;
        this.albums = albums;
        this.merchandise = merchandise;
        this.events = events;
    }

    @Override
    public String printPage() {
        // When printing the artist's page, we will take cases for each type of content
        // and concatenate them into a single string; we use StringBuilder functions
        String printString = "";

        // Albums case
        StringBuilder albumsString = new StringBuilder("Albums:\n\t[");
        for (Album album : albums) {
            albumsString.append(album.getName());
            if (albums.indexOf(album) < albums.size() - 1) {
                albumsString.append(", ");
            }
        }

        albumsString.append("]\n\n");
        printString += albumsString;

        // Merchandise case
        StringBuilder merchandiseString = new StringBuilder("Merch:\n\t[");
        for (Merchandise merch : merchandise) {
            merchandiseString.append(merch.getName()).append(" - ")
                    .append(merch.getPrice())
                    .append(":\n\t").append(merch.getDescription());
            if (merchandise.indexOf(merch) < merchandise.size() - 1) {
                merchandiseString.append(", ");
            }
        }

        merchandiseString.append("]\n\n");
        printString += merchandiseString;

        // Events case
        StringBuilder eventsString = new StringBuilder("Events:\n\t[");
        for (Event event : events) {
            eventsString.append(event.getName()).append(" - ")
                    .append(event.getDate())
                    .append(":\n\t").append(event.getDescription());
            if (events.indexOf(event) < events.size() - 1) {
                eventsString.append(", ");
            }
        }

        eventsString.append("]");
        printString += eventsString;

        return printString;
    }
}
