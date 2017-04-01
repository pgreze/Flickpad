package fr.pgreze.flickpad.data.flickr.model;

import com.google.auto.value.AutoValue;

import java.util.Map;

/**
 * Full user representation.
 * Root object: "person"
 */
@AutoValue
public abstract class FlickrFullUser {

    /** User id */
    public abstract String nsid();
    /** User name */
    public abstract Map<String, Map<String, String>> username();
    /** User real name */
    public abstract Map<String, Map<String, String>> realname();

    /** Icon server */
    public abstract String iconserver();
    /** Icon farm */
    public abstract int iconfarm();

    public static FlickrFullUser create(String nsid, Map<String, Map<String, String>> username,
                                        Map<String, Map<String, String>> realname,
                                        String iconserver, int iconfarm) {
        return new AutoValue_FlickrFullUser(nsid, username, realname, iconserver, iconfarm);
    }

    /** Example:

    "id": "64974314@N08",
    "nsid": "64974314@N08",
    "ispro": 0,
    "can_buy_pro": 0,
    "iconserver": "7174",
    "iconfarm": 8,
    "path_alias": null,
    "has_stats": "0",
    "username": {
        "_content": "Sieboldianus"
    },
    "realname": {
        "_content": "Alexander Dunkel"
    },
    "location": {
        "_content": "San Francisco, CA"
    },
    "timezone": {
        "label": "Pacific Time (US & Canada); Tijuana",
                "offset": "-08:00",
                "timezone_id": "PST8PDT"
    },
    "description": {
        "_content": "A photographing landscape architect and regional planner with interest in programming, 3d animation and social media.. and surfing (the ocean)."
    },
    "photosurl": {
        "_content": "https://www.flickr.com/photos/64974314@N08/"
    },
    "profileurl": {
        "_content": "https://www.flickr.com/people/64974314@N08/"
    },
    "mobileurl": {
        "_content": "https://m.flickr.com/photostream.gne?id=64881501"
    },
    "photos": {
        "firstdatetaken": {
            "_content": "2012-01-14 10:51:15"
        },
        "firstdate": {
            "_content": "1326567075"
        },
        "count": {
            "_content": 71
        }
    }
    */
}
