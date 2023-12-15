**&copy;** Munteanu Eugen 325CA 2023-2024
&nbsp;

## Programare Orientata pe Obiecte (POO) - Proiect - Etapa 2

***Note: The official solution for the first stage of the project was used
as a base for the second stage.***

_Generative AI was used as a tool for learning to work with streams,
refactoring code for better readability and efficiency, as well as getting
along with SimpleDateFormat and StringBuilder documentation.
It will be further discussed below (see [Generative AI](#generative-ai) and
[Encountered problems](#encountered-problems))._

---

The following project is continuing the implementation of an Audio Player,
similar to Spotify, processing audio-related input data and working with
*.json* files, outputting the results in the same format.

The design behind the project is splitting multiple tasks into smaller ones,
creating smaller and bigger classes depending on the part of the current task,
as well as using **abstraction**, **inheritance** and **polymorphism**.
A **design pattern** from the start is recommended, thus **Singleton** pattern
was used for ```Admin``` class and **Factory** pattern was implemented when
having to create a specific type of *user* (normal user, artist or host), in
```UsersFactory``` class. Those will be explained below.

---

### Implementation & OOP principles

Following the structure for the official solution for the first stage, each
input command has its own called function in ```CommandRunner``` class. While
those functions work by correctly outputting the results and messages to
the *.json* files, the main work is further implemented in the ```Admin```
class or ```User``` class, with the respective methods for each command.

Next, depending on the command, the corresponding function would be implemented
for the specific type of user (e.g. *addPodcasts*, *switchConnectionStatus*,
*addAlbum*, *removeMerch*) or, if the command is not mainly user-specific,
the function would be implemented in ```Admin``` class (e.g. *getTop5albums*,
*getAllUsers*, *showAlbums*).

Of course, some of the already implemented functions from the first stage
were modified to fit the new requirements: new search filters were added,
new types of users were added, ```@Getter``` and ```@Setter``` annotations
for some already-defined fields were used and so on.

&nbsp;

Let's take some examples for some
**OOP principles** used in this project:

Knowing that two new types of users will be added, ```Artist``` and ```Host```,
we used **inheritance** (extending ```User``` class) for each of them,
overriding the methods that are specific to each type of user if necessary and
outputting the results accordingly. It is easier to implement and keep the
order of messages needed to be shown:

```java
public final class Artist extends User {
    // Getters and Setters (Lombok)
    private final ArrayList<Album> albums = new ArrayList<>();
    
    private final ArrayList<Event> events = new ArrayList<>();
    
    private final ArrayList<Merchandise> merchandise = new ArrayList<>();
    
    ...
}
```

```java
public final class Host extends User {
    // Getters and Setters (Lombok)
    private final ArrayList<Podcast> podcasts = new ArrayList<>();
    
    private final ArrayList<Announcement> announcements = new ArrayList<>();
    
    ...
}
```

&nbsp;

Another principle applied in this context is **polymorphism**. For example,
in many commands, the output message or operations will be different depending
on the user type. Several methods were overridden in ```Artist``` and ```Host```
classes, for instance:

```User``` implementation:
```java
public String addEvent(final CommandInput commandInput) {
        return commandInput.getUsername() + " is not an artist.";
}

public String addAnnouncement(final CommandInput commandInput) {
        return commandInput.getUsername() + " is not a host.";
}
```

```Artist``` implementation:
```java
@Override
public String addEvent(final CommandInput commandInput){
        Event newEvent = new Event(...);
        
        if (...) {
        ...
        }
        
        ...
}
```

```Host``` implementation:
```java
@Override
public String addAnnouncement(final CommandInput commandInput) {
        Announcement newAnnouncement = new Announcement(...);
        
        if (...) {
        ...
        }
        
        ...
}
```

&nbsp;

On the other hand, **abstraction** was used for the page system, having an
abstract ```Page``` class and multiple classes that extend it (```HomePage```,
```LikedContentPage```, ```ArtistPage```, ```HostPage```). This way, the
*PrintCurrentPage* command can be easily implemented using overriding and
properly printing the page in the required format.

```java
public abstract class Page {
    private User pageOwner;
    
    ...
    public abstract String printPage();
}
```

---

&nbsp;

### Design Patterns

In this project, the **Singleton**  pattern was used for ```Admin``` class
(only one instance of this class is needed; the admin's view for the platform):

```java
public final class Admin {
    private static Admin admin = new Admin();

    private Admin() {
    }
    
    ...
}
```
&nbsp;

Another example of **design pattern** used in this project is **Factory**.
The ```UsersFactory``` class is used for creating new users, returning the
type of user depending on the input data:
```java
public final class UsersFactory {
    private UsersFactory() {
    }
    
    public static User createUser(final CommandInput commandInput) {
        String type = commandInput.getType();

        switch (type) {
            ...
        }
    }
}
```

---

&nbsp;

#### Generative AI

While implementing the project, I was interested in other ways of rewriting
functions and filtering data, so I found out about **streams**. Generative AI
was used as a tool for learning to work with streams overall; examples
of using **streams** can be found in *addEvent* and *addMerch* methods for
artist, *deleteUser* command or in *getAllUsers* function.

&nbsp;

### Encountered problems

* One of the most difficult commands to implement was the *deleteUser* command,
  especially passing input test no. 14. I had to reimplement methods
  again and again and carefully analyze the .json diff results to see what
  was wrong. Depending on the type of user that is being deleted, helper
  functions were needed to properly check if the deletion can be done safely.


* Another problem was when trying to properly verify the date format in
  *addEvent* case. I used ```SimpleDateFormat``` for this purpose,
  along with another created class ```ValidDateConstants``` for date constants,
  avoiding magic numbers. Generative AI was used for sample practical examples
  of ```SimpleDateFormat``` documentation.


* I also encountered an issue when implementing *PrintCurrentPage* command,
  about the way of printing the corresponding String page format. This is when
  I became familiar with ```StringBuilder``` usage, implementing the abstract
  *printPage()* method in each different page. Likewise, Generative AI was
  used for sample practical examples of ```StringBuilder``` documentation.
