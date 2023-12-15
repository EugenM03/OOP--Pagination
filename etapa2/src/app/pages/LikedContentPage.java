package app.pages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import lombok.Getter;

import java.util.ArrayList;

public final class LikedContentPage extends Page {
    @Getter
    private final ArrayList<Song> likedSongs;
    @Getter
    private final ArrayList<Playlist> likedPlaylists;

    public LikedContentPage() {
        likedSongs = new ArrayList<>();
        likedPlaylists = new ArrayList<>();
    }

    /**
     * Instantiates a new liked content page.
     *
     * @param likedSongs     the liked songs
     * @param likedPlaylists the liked playlists
     */
    public LikedContentPage(final ArrayList<Song> likedSongs,
                            final ArrayList<Playlist> likedPlaylists) {
        this.likedSongs = likedSongs;
        this.likedPlaylists = likedPlaylists;
    }

    @Override
    public String printPage() {
        // Printing the LikedContentPage - we will take cases for each type of content
        // and concatenate them into a single string; we use StringBuilder functions
        String printString = "";

        StringBuilder songsString = new StringBuilder("Liked songs:\n\t[");
        // Songs case
        for (Song song : likedSongs) {
            songsString.append(song.getName()).append(" - ").append(song.getArtist());
            if (likedSongs.indexOf(song) < likedSongs.size() - 1) {
                songsString.append(", ");
            }
        }

        songsString.append("]\n\n");
        printString += songsString;

        // Playlists case
        StringBuilder playlistsString = new StringBuilder("Followed playlists:\n\t[");
        for (Playlist playlist : likedPlaylists) {
            playlistsString.append(playlist.getName()).append(" - ").append(playlist.getOwner());
            if (likedPlaylists.indexOf(playlist) < likedPlaylists.size() - 1) {
                playlistsString.append(", ");
            }
        }

        playlistsString.append("]");
        printString += playlistsString;

        return printString;
    }
}
