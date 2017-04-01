package fr.pgreze.flickpad.domain.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.Date;

@AutoValue
public abstract class Photo {

    /** Photo id */
    public abstract String id();
    /** Photo title */
    public abstract String title();
    /** When photo was added */
    public abstract Date dateAdded();
    /** Owner id */
    public abstract String ownerId();
    /** Medium photo url (should be used in list) */
    public abstract String mediumUrl();
    /** Large photo url (should be used in fullscreen) */
    @Nullable
    public abstract String largeUrl();

    public static Photo create(String id, String title, Date dateAdded, String ownerId, String mediumUrl, String largeUrl) {
        return new AutoValue_Photo(id, title, dateAdded, ownerId, mediumUrl, largeUrl);
    }
}
