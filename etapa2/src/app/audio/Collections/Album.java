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
    private final int releaseYear;
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
                 final int releaseYear, final String description) {
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
     * @return the track by the respective index
     */
    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

    /**
     * Filter for album.
     *
     * @param name the name filter
     * @return the boolean
     */
    @Override
    public boolean matchesDescription(final String name) {
        return this.getDescription().toLowerCase().startsWith(name);
    }

    /**
     * Gets total likes for an album (i.e. the sum of likes for all songs on the album).
     *
     * @return the total likes
     */
    public int getLikes() {
        int totalLikes = 0;

        for (Song song : songs) {
            totalLikes += song.getLikes();
        }

        return totalLikes;
    }
}
