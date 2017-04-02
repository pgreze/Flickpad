package fr.pgreze.flickpad.domain.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Group implements Parcelable {

    /** Group id */
    public abstract String id();
    /** Group name */
    public abstract String name();
    /** Group thumbnail url */
    public abstract String thumbnail();
    /** Member count */
    public abstract int memberCount();
    /** Topic count */
    public abstract int topicCount();

    public static Group create(String id, String name, String thumbnail, int memberCount, int topicCount) {
        return new AutoValue_Group(id, name, thumbnail, memberCount, topicCount);
    }
}
