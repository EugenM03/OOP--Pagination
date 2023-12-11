package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Album extends AudioCollection {
    // We add the corresponding fields for properly outputting in the .json file.
    @Getter
    private final List<Song> songs;
    @Getter
    private final Integer releaseYear;
    @Getter
    private final String description;

    /**
     * Instantiates a new Album.
     *
     * @param name        the name
     * @param owner       the owner
     * @param songs       the songs
     * @param releaseYear the release year
     * @param description the description
     */
    public Album(final String name, final String owner, final ArrayList<Song> songs,
                 final Integer releaseYear, final String description) {
        super(name, owner);
        this.songs = songs;
        this.releaseYear = releaseYear;
        this.description = description;
    }

    /**
     * Gets number of tracks.
     *
     * @return the number of tracks
     */
    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    /**
     * Gets track by index.
     *
     * @param index the index
     * @return the track by index
     */
    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

    /**
     * Filter for album string.
     *
     * @param name the name filter
     * @return the string
     */
    @Override
    public boolean matchesDescription(final String name) {
        return this.getDescription().toLowerCase().startsWith(name);
    }
}
