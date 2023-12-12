package app.user;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public final class Host extends User {
    @Getter
    ArrayList<Podcast> podcasts = new ArrayList<>();
    @Getter
    ArrayList<Announcement> announcements = new ArrayList<>();

    public Host(String username, int age, String city, String userType) {
        super(username, age, city, userType);
//        super.setPage(new HostPage(this.podcasts, this.announcements, this)); TODO
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

    @Override
    public String addPodcast(CommandInput commandInput) {
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
            return commandInput.getUsername() + " has the same episode at least twice in this podcast.";
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
}
