package app.pages;

import app.audio.Collections.Podcast;
import app.users.host.Announcement;
import app.users.host.Host;
import lombok.Getter;

import java.util.ArrayList;

public final class HostPage extends Page {
    @Getter
    private final ArrayList<Podcast> podcasts;
    @Getter
    private final ArrayList<Announcement> announcements;
    @Getter
    private Host pageOwner;

    public HostPage() {
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
    }

    /**
     * Instantiates a new host page.
     *
     * @param pageOwner     the host that owns the page
     * @param podcasts      the podcasts
     * @param announcements the announcements
     */
    public HostPage(final Host pageOwner, final ArrayList<Podcast> podcasts,
                    final ArrayList<Announcement> announcements) {
        this.pageOwner = pageOwner;
        this.podcasts = podcasts;
        this.announcements = announcements;
    }

    @Override
    public String printPage() {
        // When printing the host's page, we will take cases for each type of content
        // and concatenate them into a single string; we use StringBuilder functions
        String printString = "";
        StringBuilder podcastsString = new StringBuilder("Podcasts:\n\t[");

        // Podcasts case
        for (Podcast podcast : podcasts) {
            podcastsString.append(podcast.getName()).append(":\n\t[");
            for (int j = 0; j < podcast.getEpisodes().size(); j++) {
                podcastsString.append(podcast.getEpisodes().get(j).getName()).append(" - ")
                        .append(podcast.getEpisodes().get(j).getDescription());
                if (j < podcast.getEpisodes().size() - 1) {
                    podcastsString.append(", ");
                }
                if (j == podcast.getEpisodes().size() - 1) {
                    podcastsString.append("]\n");
                }
            }
            if (podcasts.indexOf(podcast) < podcasts.size() - 1) {
                podcastsString.append(", ");
            }
        }

        podcastsString.append("]\n\n");
        printString += podcastsString;

        // Announcements case
        StringBuilder announcementsString = new StringBuilder("Announcements:\n\t[");
        for (Announcement announcement : announcements) {
            announcementsString.append(announcement.getName()).append(":\n\t")
                    .append(announcement.getDescription());
            if (announcements.indexOf(announcement) < announcements.size() - 1) {
                announcementsString.append("\n, ");
            }
            if (announcements.indexOf(announcement) == announcements.size() - 1) {
                announcementsString.append("\n");
            }
        }

        announcementsString.append("]");
        printString += announcementsString;

        return printString;
    }
}

