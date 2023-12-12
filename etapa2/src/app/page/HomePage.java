package app.page;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;

public final class HomePage extends Page {
    // A particular page that displays the user's liked songs and followed playlists.
    @Getter
    private ArrayList<Song> recommendedSongs;
    @Getter
    private ArrayList<Playlist> recommendedPlaylists;

    public HomePage() {
        recommendedSongs = new ArrayList<>();
        recommendedPlaylists = new ArrayList<>();
    }

    public HomePage(final ArrayList<Song> recommendedSongs, final ArrayList<Playlist> followedPlaylists) {
        this.recommendedSongs = recommendedSongs;
        this.recommendedPlaylists = followedPlaylists;
    }

    @Override
    public String printPage() {
        ArrayList<String> topSongs = new ArrayList<>();
        int results = 0;

        // We iterate through the top liked songs and
        // add them to the topSongs list (5 maximum).
        Iterator<Song> songIterator = recommendedSongs.iterator();

        while (songIterator.hasNext() && results < MAX_RESULTS) {
            Song song = songIterator.next();
            topSongs.add(song.getName());
            results++;
        }

        // We iterate through the top playlists and
        // add them to the topPlaylists list (5 maximum).
        ArrayList<String> topPlaylists = new ArrayList<>();
        Iterator<Playlist> playlistIterator = this.recommendedPlaylists.iterator();

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
