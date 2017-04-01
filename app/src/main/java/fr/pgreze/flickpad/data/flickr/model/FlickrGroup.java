package fr.pgreze.flickpad.data.flickr.model;

import com.google.auto.value.AutoValue;

import fr.pgreze.flickpad.common.model.AutoGson;

/**
 * Group representation returned during pagination.
 */
@AutoValue
@AutoGson(autoValueClass = AutoValue_FlickrGroup.class)
public abstract class FlickrGroup {

    /** Group id */
    public abstract String nsid();
    /** Group name */
    public abstract String name();
    /** 0 if false */
    public abstract int eighteenplus();
    /** Icon server */
    public abstract String iconserver();
    /** Icon farm */
    public abstract int iconfarm();
    /** Member count */
    public abstract String members();
    public abstract String poolCount();
    /** Topic count */
    public abstract String topicCount();
    /** Privacy level. Always 3 */
    public abstract String privacy();

    public static FlickrGroup create(String nsid, String name, int eighteenplus,
                                     String iconserver, int iconfarm, String members,
                                     String poolCount, String topicCount, String privacy) {
        return new AutoValue_FlickrGroup(nsid, name, eighteenplus, iconserver, iconfarm,
                members, poolCount, topicCount, privacy);
    }


    /** Example:

    "nsid": "51035612836@N01",
    "name": "Flickr API",
    "eighteenplus": 0,
    "iconserver": "3686",
    "iconfarm": 4,
    "members": "17675",
    "pool_count": "930",
    "topic_count": "4166",
    "privacy": "3"
    */
}
