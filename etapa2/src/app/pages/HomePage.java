package app.pages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public final class HomePage extends Page {
    // A particular page that displays the user's liked songs and followed playlists.
    @Getter
    private final ArrayList<Song> recommendedSongs;
    @Getter
    private final ArrayList<Playlist> recommendedPlaylists;

    public HomePage() {
        recommendedSongs = new ArrayList<>();
        recommendedPlaylists = new ArrayList<>();
    }

    /**
     * Instantiates a new home page.
     *
     * @param recommendedSongs     the recommended songs
     * @param recommendedPlaylists the recommended playlists
     */
    public HomePage(final ArrayList<Song> recommendedSongs,
                    final ArrayList<Playlist> recommendedPlaylists) {
        this.recommendedSongs = recommendedSongs;
        this.recommendedPlaylists = recommendedPlaylists;
    }

    @Override
    public String printPage() {
        // Printing top 5 liked songs and top 5 followed playlists, as required
        // We use a copy of the current lists to not modify the original ones
        ArrayList<String> topSongs = new ArrayList<>();
        ArrayList<Song> copiedRecommendedSongs = new ArrayList<>(recommendedSongs);
        int results = 0;

        // Sort by likes in descending order
        Collections.sort(copiedRecommendedSongs, Comparator.comparing(Song::getLikes).reversed());

        // We iterate through the top liked songs and
        // add them to the topSongs list (5 maximum).
        Iterator<Song> songIterator = copiedRecommendedSongs.iterator();

        while (songIterator.hasNext() && results < MAX_RESULTS) {
            Song song = songIterator.next();
            topSongs.add(song.getName());
            results++;
        }

        results = 0;

        // We iterate through the top playlists and
        // add them to the topPlaylists list (5 maximum).
        ArrayList<String> topPlaylists = new ArrayList<>();
        ArrayList<Playlist> copiedRecommendedPlaylists = new ArrayList<>(recommendedPlaylists);

        // Sort alphabetically
        Collections.sort(copiedRecommendedPlaylists, Comparator.comparing(Playlist::getName));

        Iterator<Playlist> playlistIterator = copiedRecommendedPlaylists.iterator();

        while (playlistIterator.hasNext() && results < MAX_RESULTS) {
            Playlist playlist = playlistIterator.next();
            topPlaylists.add(playlist.getName());
            results++;
        }

        // Return the required output format
        return "Liked songs:\n\t" + topSongs
                + "\n\n" + "Followed playlists:\n\t" + topPlaylists;
    }
}
