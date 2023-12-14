package app.pages;

import app.users.User;
import lombok.Getter;

public abstract class Page {
    // Contains the user that owns the page and the page's content
    protected static final int MAX_RESULTS = 5;
    @Getter
    private User pageOwner;

    public Page() {
    }

    public Page(final User pageOwner) {
        this.pageOwner = pageOwner;
    }

    /**
     * Prints the page's content
     *
     * @return the page's content
     */
    public abstract String printPage();

}
