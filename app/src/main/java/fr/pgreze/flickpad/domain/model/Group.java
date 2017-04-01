package fr.pgreze.flickpad.domain.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Group {

    /** Group id */
    public abstract String id();
    /** Group name */
    public abstract String name();
    /** Icon server */
    public abstract String iconServer();
    /** Icon farm */
    public abstract int iconFarm();
    /** Member count */
    public abstract String memberCount();
    /** Topic count */
    public abstract String topicCount();

    public static Group create(String id, String name, String iconServer, int iconFarm,
                               String members, String topicCount) {
        return new AutoValue_Group(id, name, iconServer, iconFarm, members, topicCount);
    }
}
