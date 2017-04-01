package fr.pgreze.flickpad.domain.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class User {

    /** User id */
    public abstract String id();
    /** User pseudo */
    public abstract String userName();
    /** User real name */
    public abstract String realName();
    /** User's thumbnail */
    @Nullable
    public abstract String thumbnail();

    public static User create(String id, String userName, String realName, String thumbnail) {
        return new AutoValue_User(id, userName, realName, thumbnail);
    }
}
