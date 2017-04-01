package fr.pgreze.flickpad.domain.model;

import com.google.auto.value.AutoValue;

import java.util.Date;

@AutoValue
public abstract class Photo {

    public abstract String id();

    public abstract String title();

    public abstract Date dateAdded();

    // Owner

    public abstract String ownerId();

    public abstract String ownerName();

    // Url resolution

    public abstract String secret();

    public abstract int farm();

    public abstract String server();

    public static Photo create(String id, String title, Date dateAdded,
                               String ownerId, String ownerName,
                               String secret, int farm, String server) {
        return new AutoValue_Photo(id, title, dateAdded, ownerId, ownerName, secret, farm, server);
    }
}
