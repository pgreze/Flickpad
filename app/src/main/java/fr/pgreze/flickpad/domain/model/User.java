package fr.pgreze.flickpad.domain.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class User {

    public abstract String id();

    public abstract String userName();

    public abstract String realName();

    public abstract String iconServer();

    public abstract int iconFarm();

    public static User create(String id, String userName, String realName, String iconServer, int iconFarm) {
        return new AutoValue_User(id, userName, realName, iconServer, iconFarm);
    }
}
