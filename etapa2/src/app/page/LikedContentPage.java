package app.page;

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

    public LikedContentPage(final ArrayList<Song> likedSongs,
                            final ArrayList<Playlist> likedPlaylists) {
        this.likedSongs = likedSongs;
        this.likedPlaylists = likedPlaylists;
    }

    @Override
    public String printPage() {
        String printString = "";
        StringBuilder songsString = new StringBuilder("Liked songs:\n\t[");
        for (Song song : likedSongs) {
            songsString.append(song.getName()).append(" - ").append(song.getArtist());
            if (likedSongs.indexOf(song) < likedSongs.size() - 1) {
                songsString.append(", ");
            }
        }

        songsString.append("]\n\n");
        printString += songsString;

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
