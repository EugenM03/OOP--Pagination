package app.user;

import app.Admin;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.page.HostPage;
import app.player.Player;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public final class Host extends User {
    @Getter
    private final ArrayList<Podcast> podcasts = new ArrayList<>();
    @Getter
    private final ArrayList<Announcement> announcements = new ArrayList<>();

    public Host(final String username, final int age, final String city, final String userType) {
        super(username, age, city, userType);
        super.setPage(new HostPage(this, this.podcasts, this.announcements));
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
     * Adds  podcast.
     *
     * @param commandInput the command input
     * @return the output message
     */
    @Override
    public String addPodcast(final CommandInput commandInput) {
        // Convert to episode inputs Episodes objects
        ArrayList<Episode> episodes = new ArrayList<>();
        commandInput.getEpisodes().forEach(episodeInput ->
                episodes.add(new Episode(
                        episodeInput.getName(),
                        episodeInput.getDuration(),
                        episodeInput.getDescription()
                ))
        );

        // Creating the podcast and checking its validity
        Podcast newPodcast = new Podcast(commandInput.getName(),
                commandInput.getUsername(), episodes);

        // Check if the host already has a podcast with the same name
        if (this.podcasts.stream().anyMatch(podcast -> podcast.matchesName(newPodcast.getName()))) {
            return commandInput.getUsername() + " has another podcast with the same name.";
        }

        // Check if the podcast has duplicate episodes
        Set<String> episodeNames = newPodcast.getEpisodes().stream()
                .map(Episode::getName)
                .collect(Collectors.toSet());

        if (episodeNames.size() < newPodcast.getEpisodes().size()) {
            return commandInput.getUsername()
                    + " has the same episode at least twice in this podcast.";
        }

        // If the podcast is valid, add it to the list of podcasts (no duplicates allowed)
        // and afterwards we add the podcast to the host's list of podcasts
        Set<String> podcastNames = this.podcasts.stream()
                .map(Podcast::getName)
                .collect(Collectors.toSet());

        // Finally, we add the podcast to the corresponding list and return the output message
        Admin.addPodcast(newPodcast);
        this.podcasts.add(newPodcast);
        return commandInput.getUsername() + " has added new podcast successfully.";
    }

    @Override
    public String removePodcast(final CommandInput commandInput) {
        // Trying to found the podcast with the given name
        Podcast foundPodcast = this.podcasts.stream()
                .filter(podcast -> podcast.matchesName(commandInput.getName()))
                .findFirst()
                .orElse(null);

        if (foundPodcast != null) {
            // Check if we can delete the podcast safely
            boolean deletePodcast = true;
            for (User currUser : Admin.getUsers()) {
                Player currPlayer = currUser.getPlayer();
                if (currPlayer != null
                        && currPlayer.getSource() != null
                        && currPlayer.getSource().getAudioCollection() != null
                        && currPlayer.getSource().getAudioCollection()
                        .matchesName(foundPodcast.getName())) {
                    deletePodcast = false;
                    break;
                }
            }

            if (deletePodcast) {
                // If we can delete the podcast, we remove it from the list of podcasts
                this.podcasts.remove(foundPodcast);
                return commandInput.getUsername() + " deleted the podcast successfully.";
            } else {
                // We can't delete the podcast
                return commandInput.getUsername() + " can't delete this podcast.";
            }
        } else {
            // We didn't find a podcast
            return commandInput.getUsername() + " doesn't have a podcast with the given name.";
        }
    }

    /**
     * Adds announcement.
     *
     * @param commandInput the command input
     * @return the output message
     */
    @Override
    public String addAnnouncement(final CommandInput commandInput) {
        // Creating the announcement and checking its validity
        Announcement newAnnouncement = new Announcement(commandInput.getName(),
                commandInput.getDescription());

        // Check if the host already has an announcement with the same name
        if (this.announcements.stream().
                anyMatch(announcement -> announcement.matchesName(newAnnouncement.getName()))) {
            return commandInput.getUsername() + " has another announcement with the same name.";
        }

        // If the announcement is valid, add it to the list of announcements
        this.announcements.add(newAnnouncement);
        return commandInput.getUsername() + " has successfully added new announcement.";
    }

    /**
     * Removes announcement.
     *
     * @param commandInput the command input
     * @return the output message
     */
    @Override
    public String removeAnnouncement(final CommandInput commandInput) {
        // Trying to found the announcement with the given name
        boolean found = announcements.removeIf(announcement ->
                announcement.getName().equals(commandInput.getName()));

        if (found) {
            return commandInput.getUsername() + " has successfully deleted the announcement.";
        } else {
            return commandInput.getUsername() + " has no announcement with the given name.";
        }
    }
}
