package app.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public final class AlbumOutput {
    // We use the following fields to properly display the results of the "showAlbums" command.
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private List<String> songs;
}
