package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.Player;
import app.player.PlayerSource;
import app.users.artist.Artist;
import app.users.host.Host;
import app.users.User;
import app.users.UsersFactory;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Admin.
 */
public final class Admin {
    private static final int LIMIT = 5;
    // Singleton pattern - we only need one instance of Admin
//    @Getter
    @Getter
    private static final Admin instance = new Admin();

    @Getter
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

        for (User currUser : Admin.users) {
            if ("artist".equals(currUser.getUserType())) {
                albums.addAll(((Artist) currUser).getAlbums());
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

        for (User currUser : Admin.users) {
            if ("artist".equals(currUser.getUserType())) {
                artists.add((Artist) currUser);
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
     * Deletes a user.
     *
     * @param commandInput the command input
     * @return the string
     */
    public static String deleteUser(final CommandInput commandInput) {
        User currUser = Admin.getUser(commandInput.getUsername());
        if (currUser == null) {
            // If the user doesn't exist
            return "User " + commandInput.getUsername() + " does not exist.";
        }

        // Depending on the user type, we have many conditions and cases to take care for
        switch (currUser.getUserType()) {
            case "user" -> {
                // Normal user case
                // We check if the user can be removed from the list safely
                if (canDeleteNormalUser(currUser)) {
                    // We update the number of likes and followers for the playlists and songs
                    for (Playlist currPlaylist : currUser.getFollowedPlaylists()) {
                        currPlaylist.decreaseFollowers();
                    }
                    for (Song currSong : currUser.getLikedSongs()) {
                        currSong.dislike();
                    }

                    // If playlists from the user are also followed by other users, we decrease
                    // the number of followers for those playlists as well
                    users.stream()
                            .filter(otherUser -> otherUser != currUser)
                            .forEach(otherUser ->
                                    otherUser.getFollowedPlaylists().removeIf(playlist ->
                                            currUser.getPlaylists().contains(playlist)));

                    // We remove the user from the list
                    users.remove(currUser);
                    return currUser.getUsername() + " was successfully deleted.";
                } else {
                    // The user cannot be deleted
                    return currUser.getUsername() + " can't be deleted.";
                }
            }

            case "artist" -> {
                // Artist case
                // We check if the artist can be removed from the list safely
                Artist currArtist = (Artist) currUser;
                if (canDeleteArtist(currArtist)) {
                    // Delete all the artist's albums; for each album we delete all the songs
                    for (Album album : currArtist.getAlbums()) {
                        List<String> songsNames = new ArrayList<>();
                        for (Song song : album.getSongs()) {
                            songsNames.add(song.getName());
                        }

                        // Delete all the songs from the list, as well as
                        // from other users' liked songs and from their playlists
                        for (User otherUser : users) {
                            otherUser.getLikedSongs().removeIf(song
                                    -> songsNames.contains(song.getName()));
                            for (Playlist playlist : otherUser.getPlaylists()) {
                                playlist.getSongs().removeIf(song
                                        -> songsNames.contains(song.getName()));
                            }
                        }

                        // Delete all the songs from the list
                        for (Song currSong : album.getSongs()) {
                            songs.remove(currSong);
                        }
                    }
                    // Delete the artist from the list
                    users.remove(currArtist);
                    return currArtist.getUsername() + " was successfully deleted.";
                } else {
                    return currArtist.getUsername() + " can't be deleted.";
                }
            }

            case "host" -> {
                // Host case
                // We check if the host can be removed from the list safely
                Host currHost = (Host) currUser;
                if (canDeleteHost(currHost)) {
                    List<String> podcastsNames = new ArrayList<>();
                    for (Podcast podcast : currHost.getPodcasts()) {
                        podcastsNames.add(podcast.getName());
                    }

                    // Then, delete all the podcasts from the list
                    podcasts.removeIf(podcast -> podcastsNames.contains(podcast.getName()));

                    // Delete the host from the list
                    users.remove(currHost);

                    return currHost.getUsername() + " was successfully deleted.";
                } else {
                    return currHost.getUsername() + " can't be deleted.";
                }
            }
            default -> {
                // Invalid user type
                return currUser.getUsername() + " can't be deleted.";
            }
        }
    }

    /**
     * Verifies if a user (normal user) can be deleted.
     *
     * @param currUser the user to be deleted
     * @return true if the user can be deleted, false otherwise
     */
    private static boolean canDeleteNormalUser(final User currUser) {
        // We need to check if all the playlists created by the user can be deleted safely;
        // for instance, another user may be playing a song from one of the users' playlist
        for (Playlist currPlaylist : currUser.getPlaylists()) {
            for (User user : users) {
                Player currPlayer = user.getPlayer();
                if (currPlayer != null
                        && currPlayer.getSource() != null
                        && currPlayer.getSource().getAudioCollection() != null
                        && currPlayer.getSource().getAudioCollection()
                        .getName().matches(currPlaylist.getName())) {
                    // The user's playlist is currently being played by another user
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Verifies if a user (artist) can be deleted.
     *
     * @param currArtist the artist to be deleted
     * @return true if the artist can be deleted, false otherwise
     */
    private static boolean canDeleteArtist(final Artist currArtist) {
        // We need to check if all the albums created by the artist can be deleted safely;
        // for instance, another user may be playing a song from one of the users' albums
        for (Album currAlbum : currArtist.getAlbums()) {
            for (User user : users) {
                // Check if the album is currently being played by another user
                Player currPlayer = user.getPlayer();
                if (currPlayer != null
                        && currPlayer.getSource() != null
                        && currPlayer.getSource().getAudioCollection() != null
                        && currPlayer.getSource().getAudioCollection()
                        .getName().matches(currAlbum.getName())) {
                    return false;
                }

                // Check if the source is a playlist currently playing a song from the album
                if (currPlayer != null
                        && currPlayer.getSource() != null
                        && currPlayer.getSource().getAudioCollection() != null) {
                    int numTracks = currPlayer.getSource().getAudioCollection().getNumberOfTracks();
                    for (int i = 0; i < numTracks; i++) {
                        if (currPlayer.getSource().getAudioCollection().getTrackByIndex(i)
                                .getName().matches(currAlbum.getName())) {
                            return false;
                        }
                    }
                }

                // Lastly, check if a song from the album is currently playing
                for (Song currSong : currAlbum.getSongs()) {
                    if (currPlayer != null
                            && currPlayer.getSource() != null
                            && currPlayer.getSource().getAudioFile() != null
                            && currPlayer.getSource().getAudioFile()
                            .getName().matches(currSong.getName())) {
                        return false;
                    }
                }
            }
        }

        // Lastly, we need to check if the current page of the artist
        // is being accessed by another user
        for (User currUser : users) {
            User currOwner = currUser.getPage().getPageOwner();
            if (!currArtist.getUsername().matches(currUser.getUsername())
                    && currOwner != null
                    && currOwner.getUsername().matches(currArtist.getUsername())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Verifies if a user (host) can be deleted.
     *
     * @param currHost the host to be deleted
     * @return true if the host can be deleted, false otherwise
     */
    private static boolean canDeleteHost(final Host currHost) {
        // We need to check if all the podcasts created by the host can be deleted safely;
        // for instance, another user may be playing a podcast from one of the users' podcasts
        for (Podcast currPodcast : currHost.getPodcasts()) {
            for (User currUser : users) {
                PlayerSource currSource = currUser.getPlayer().getSource();
                if (currSource != null
                        && currSource.getAudioCollection() != null
                        && currSource.getAudioCollection().getName()
                        .matches(currPodcast.getName())) {
                    // The user is currently playing a podcast from the host
                    return false;
                }
            }
        }

        // Lastly, check if the current page of the host is being accessed by another user
        for (User currUser : users) {
            User currOwner = currUser.getPage().getPageOwner();
            if (!currHost.getUsername().matches(currUser.getUsername())
                    && currOwner != null
                    && currOwner.getUsername().matches(currHost.getUsername())) {
                return false;
            }
        }

        return true;
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
