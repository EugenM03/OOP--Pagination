package app.page;

import app.audio.Collections.Album;
import app.user.Artist;
import app.user.Event;
import app.user.Merch;
import lombok.Getter;

import java.util.ArrayList;

public final class ArtistPage extends Page {
    @Getter
    private final ArrayList<Album> albums;
    @Getter
    private final ArrayList<Merch> merchandise;
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
                      final ArrayList<Merch> merchandise, final ArrayList<Event> events) {
        this.pageOwner = pageOwner;
        this.albums = albums;
        this.merchandise = merchandise;
        this.events = events;
    }

    @Override
    public String printPage() {
        String printString = "";
        StringBuilder albumsString = new StringBuilder("Albums:\n\t[");

        for (int i = 0; i < albums.size(); i++) {
            albumsString.append(albums.get(i).getName());
            if (i < albums.size() - 1) {
                albumsString.append(", ");
            }
        }
        albumsString.append("]");
        printString += albumsString + "\n\n";

        StringBuilder merchString = new StringBuilder("Merch:\n\t[");
        for (int i = 0; i < merchandise.size(); i++) {
            merchString.append(merchandise.get(i).getName()).append(" - ")
                    .append(merchandise.get(i).getPrice())
                    .append(":\n\t").append(merchandise.get(i).getDescription());
            if (i < merchandise.size() - 1) {
                merchString.append(", ");
            }
        }
        merchString.append("]");
        printString += merchString + "\n\n";

        StringBuilder eventsString = new StringBuilder("Events:\n\t[");
        for (int i = 0; i < events.size(); i++) {
            eventsString.append(events.get(i).getName()).append(" - ")
                    .append(events.get(i).getDate())
                    .append(":\n\t").append(events.get(i).getDescription());
            if (i < events.size() - 1) {
                eventsString.append(", ");
            }
        }
        eventsString.append("]");
        printString += eventsString;

        return printString;
    }
}
