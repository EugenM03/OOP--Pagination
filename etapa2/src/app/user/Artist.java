package app.user;

import app.audio.Collections.Album;
import app.audio.Files.Song;
import fileio.input.CommandInput;
import lombok.Getter;

import app.Admin;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class Artist extends User {
    @Getter
    private final ArrayList<Album> albums = new ArrayList<>();
    @Getter
    private final ArrayList<Event> events = new ArrayList<>();
    @Getter
    private final ArrayList<Merch> merchandise = new ArrayList<>();

    /**
     * Instantiates a new User (Artist).
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     * @param userType the type of user
     */
    public Artist(final String username, final int age, final String city, final String userType) {
        super(username, age, city, userType);
        // TODO: super.setPage(new ArtistPage(this.albums, this.events, this.merch, this));
    }

    /**
     * Switch connection status.
     *
     * @param commandInput the command input
     * @return the output message
     */
    @Override
    public String switchConnectionStatus(final CommandInput commandInput) {
        return commandInput.getUsername() + " is not a normal user.";
    }

    /**
     * Add an album (implemented for artist).
     *
     * @param commandInput the command input
     * @return the string
     */
    @Override
    public String addAlbum(final CommandInput commandInput) {
        ArrayList<Song> songs = new ArrayList<>();

        // Convert the song inputs Song objects
        commandInput.getSongs().forEach(songInput ->
                songs.add(new Song(
                        songInput.getName(),
                        songInput.getDuration(),
                        songInput.getAlbum(),
                        songInput.getTags(),
                        songInput.getLyrics(),
                        songInput.getGenre(),
                        songInput.getReleaseYear(),
                        songInput.getArtist()
                ))
        );


        // Creating the album and checking its validity
        Album newAlbum = new Album(commandInput.getName(), commandInput.getUsername(), songs,
                commandInput.getReleaseYear(), commandInput.getDescription());


        // Check if the artist already has an album with the same name
        if (this.albums.stream().anyMatch(album -> album.matchesName(newAlbum.getName()))) {
            return commandInput.getUsername() + " has another album with the same name.";
        }

        // Check if the album has duplicate songs (used ChatGPT for help on duplicating)
        Set<String> songNames = newAlbum.getSongs().stream()
                .map(Song::getName)
                .collect(Collectors.toSet());

        if (songNames.size() < newAlbum.getSongs().size()) {
            return commandInput.getUsername() + " has the same song at least twice in this album.";
        }

        // If the album is valid, we add the songs to the album (no duplicates allowed)
        // and afterwards we add the album to the artist's list of albums (ChatGPT helped)
        Set<String> songNamesInAdmin = Admin.getSongs()
                .stream()
                .map(Song::getName)
                .collect(Collectors.toSet());


        // Finally, we add the song to the corresponding list and return the output message
        Admin.addSongs(newAlbum.getSongs());
        this.albums.add(newAlbum);
        return commandInput.getUsername() + " has added new album successfully.";
    }
}

