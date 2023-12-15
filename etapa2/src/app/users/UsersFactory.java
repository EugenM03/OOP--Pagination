package app.users;

import app.users.artist.Artist;
import app.users.host.Host;
import fileio.input.CommandInput;

public final class UsersFactory {
    private UsersFactory() {
    }

    /**
     * Creates a user based on the command input
     *
     * @param commandInput the command input
     * @return the new created user
     */
    public static User createUser(final CommandInput commandInput) {
        // A small design pattern that creates a user based on the command input user type
        String type = commandInput.getType();

        switch (type) {
            case "artist":
                return new Artist(commandInput.getUsername(),
                        commandInput.getAge(), commandInput.getCity(), type);
            case "host":
                return new Host(commandInput.getUsername(),
                        commandInput.getAge(), commandInput.getCity(), type);
            default:
                return new User(commandInput.getUsername(),
                        commandInput.getAge(), commandInput.getCity());
        }
    }
}
