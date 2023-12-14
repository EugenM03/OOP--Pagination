package app.audio.Collections;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public final class PodcastOutput {
    // We use the following fields to properly display the results of the "showPodcasts" command.
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private List<String> episodes;

    public PodcastOutput() {
    }
}
