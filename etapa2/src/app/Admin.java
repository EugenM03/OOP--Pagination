package app;

import app.audio.Collections.Album;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.Player;
import app.player.PodcastBookmark;
import app.users.artist.Artist;
import app.users.host.Host;
import app.users.User;
import app.users.UsersFactory;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;

/**
 * The type Admin.
 */
public final class Admin {
    private static final int LIMIT = 5;
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static int timestamp = 0;

    private Admin() {
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public static List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public static void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                        episodeInput.getDuration(),
                        episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * Gets albums.
     *
     * @return the albums
     */
    public static List<Album> getAlbums() {
        List<Album> albums = new ArrayList<>();

        for (User user : Admin.users) {
            if ("artist".equals(user.getUserType())) {
                albums.addAll(((Artist) user).getAlbums());
            }
        }

        return albums;
    }

    /**
     * Gets artists.
     *
     * @return the artists
     */
    public static List<Artist> getArtists() {
        List<Artist> artists = new ArrayList<>();

        for (User user : Admin.users) {
            if ("artist".equals(user.getUserType())) {
                artists.add((Artist) user);
            }
        }

        return artists;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public static User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static List<User> getUsers() {
        return users;                                                                  // ??????
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public static void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            user.simulateTime(elapsed);
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Adds songs to the list.
     *
     * @param source the list of songs to be added
     */
    public static void addSongs(final List<Song> source) {
        Set<String> existingSongNames = songs.stream()
                .map(Song::getName)
                .collect(Collectors.toSet());

        source.stream()
                .filter(song -> !existingSongNames.contains(song.getName()))
                .forEach(song -> {
                    songs.add(song);
                    existingSongNames.add(song.getName());
                });
    }

    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Adds a user.
     *
     * @param commandInput the command input
     * @return the new user
     */
    public static String addUser(final CommandInput commandInput) {
        // Inspired from 'factory' design pattern, we create a new user based on the type
        // specified in the command input (artist, host, normal user)
        User newUser = UsersFactory.createUser(commandInput);

        // We add the user to the list only if it isn't already taken
        if (newUser.getUsername() != null && Admin.getUser(newUser.getUsername()) != null) {
            return "The username " + newUser.getUsername() + " is already taken.";
        }

        // Adding user to the list
        users.add(newUser);
        return "The username " + newUser.getUsername() + " has been added successfully.";
    }


    /**
     * Adds podcasts to the list.
     *
     * @param podcast the list of podcasts to be added
     */

    public static void addPodcast(final Podcast podcast) {
        if (!podcasts.contains(podcast)) {
            podcasts.add(podcast);
        }
    }

    /**
     * Gets top 5 albums.
     *
     * @return the top 5 albums
     */
    public static List<String> getTop5Albums() {
        List<Album> mostLikedAlbums = new ArrayList<>(getAlbums());

        // Sort after the total number of likes for each album, in descending order;
        // if two albums have the same number of likes, sort them alphabetically
        Collections.sort(mostLikedAlbums, Comparator.comparingInt(Album::getLikes).reversed()
                .thenComparing(Album::getName));

        // Keep only the top 5 albums
        List<String> topAlbums = mostLikedAlbums.stream()
                .limit(LIMIT)
                .map(Album::getName)
                .collect(Collectors.toList());

        return topAlbums;
    }

    /**
     * Gets top 5 artists.
     *
     * @return the top 5 artists
     */
    public static List<String> getTop5Artists() {
        List<Artist> mostLikedArtists = new ArrayList<>(Admin.getArtists());

        // Sort after the total number of likes for each artist, in descending order
        Collections.sort(mostLikedArtists, Comparator.comparingInt(Artist::getLikes).reversed());

        // Keep only the top 5 artists
        List<String> topArtists = mostLikedArtists.stream()
                .limit(LIMIT)
                .map(Artist::getUsername)
                .collect(Collectors.toList());

        return topArtists;
    }

    /**
     * Gets all users (order: normal users, artists, hosts).
     *
     * @return the users
     */
    public static List<String> getAllUsers() {
        // Using streams to get all the usernames in the list, in the required order
        List<String> allUsers = users.stream()
                .filter(user -> "user".equals(user.getUserType()))
                .map(User::getUsername)
                .collect(Collectors.toList());

        allUsers.addAll(users.stream()
                .filter(user -> "artist".equals(user.getUserType()))
                .map(User::getUsername)
                .toList());

        allUsers.addAll(users.stream()
                .filter(user -> "host".equals(user.getUserType()))
                .map(User::getUsername)
                .toList());

        return allUsers;
    }

    /**
     * Gets online users.
     *
     * @return the online users
     */
    public static List<String> getOnlineUsers() {
        return users.stream()
                .filter(User::isOnline)
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    /**
     * Reset.
     */
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }
}
