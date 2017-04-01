package fr.pgreze.flickpad.data.flickr.model;

import com.google.auto.value.AutoValue;

import fr.pgreze.flickpad.common.model.AutoGson;

/**
 * Photo light object returned during pagination.
 * Root object: "photo"
 */
@AutoValue
@AutoGson(autoValueClass = AutoValue_FlickrPhoto.class)
public abstract class FlickrPhoto {

    public abstract String id();
    public abstract String title();
    /** Owner user id */
    public abstract String owner();
    /** Used for url resolution */
    public abstract String secret();
    /** Used for url resolution */
    public abstract int farm();
    /** Used for url resolution */
    public abstract String server();

    public abstract String dateadded();
    public abstract int ispublic();

    public static FlickrPhoto create(String id, String title, String owner,
                                     String secret, int farm, String server,
                                     String dateadded, int ispublic) {
        return new AutoValue_FlickrPhoto(id, title, owner, secret, farm, server, dateadded, ispublic);
    }


    /** Example:

     "id": "17856089745",
     "owner": "64974314@N08",
     "secret": "84369f7253",
     "server": "8762",
     "farm": 9,
     "title": "Visualization of geotagged Flickr photos (Europe), 2007-2015",
     "ispublic": 1,
     "isfriend": 0,
     "isfamily": 0,
     "ownername": "Sieboldianus",
     "dateadded": "1488657072"
     */
}
