package app.user;

import app.audio.Collections.Album;
import app.audio.Files.Song;
import app.page.ArtistPage;
import fileio.input.CommandInput;
import lombok.Getter;

import app.Admin;

import java.text.SimpleDateFormat;
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
     * Instantiates a new Artist.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     * @param userType the type of user
     */
    public Artist(final String username, final int age, final String city, final String userType) {
        super(username, age, city, userType);
        super.setPage(new ArtistPage(this, this.albums, this.merchandise, this.events));
    }

    // TODO (ChatGpt helped)
    private static boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false); // Input must match the exact format

        // Check for the required conditions
        int day = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(3, 5));
        int year = Integer.parseInt(date.substring(6, 10));

        if (day < 1 || day > 31 || month < 1 || month > 12 || year < 1900 || year > 2023) {
            return false;
        }

        // Verify the day depending on the month
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return day <= 30;
        } else if (month == 2) {
            return day <= 28;
        } else {
            return true;
        }
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

    /**
     * Add a merch (implemented for artist).
     *
     * @param commandInput the command input
     * @return the string
     */
    @Override
    public String addMerch(CommandInput commandInput) {
        Merch newMerch = new Merch(commandInput.getName(),
                commandInput.getDescription(), commandInput.getPrice());

        // Check if a merch with the same name already exists
        if (merchandise.stream().anyMatch(merch -> merch.getName().equals(newMerch.getName()))) {
            return commandInput.getUsername() + " has merchandise with the same name.";
        }

        // Check if the price is negative
        if (newMerch.getPrice() < 0) {
            return "Price for merchandise can not be negative.";
        }

        // If the merch is valid, we add it to the list of merch and return the output message
        merchandise.add(newMerch);
        return commandInput.getUsername() + " has added new merchandise successfully.";
    }

    /**
     * Add an event (implemented for artist).
     *
     * @param commandInput the command input
     * @return the string
     */
    @Override
    public String addEvent(CommandInput commandInput) {
        Event newEvent = new Event(commandInput.getName(),
                commandInput.getDescription(), commandInput.getDate());

        // Check if an event with the same name already exists
        if (events.stream().anyMatch(event -> event.getName().equals(newEvent.getName()))) {
            return commandInput.getUsername() + " has another event with the same name.";
        }

        // Check if the date is valid
        if (!isValidDate(commandInput.getDate())) {
            return "Event " + commandInput.getUsername() + " does not have a valid date.";
        }

        // If the event is valid, we add it to the list of events and return the output message
        events.add(newEvent);
        return commandInput.getUsername() + " has added new event successfully.";
    }
}

