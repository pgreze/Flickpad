package fr.pgreze.flickpad.data.flickr.model;

import com.google.auto.value.AutoValue;

import java.util.List;

import fr.pgreze.flickpad.common.model.AutoGson;

@AutoValue
@AutoGson(autoValueClass = AutoValue_FlickrPhotos.class)
public abstract class FlickrPhotos {

    public abstract int page();

    public abstract int pages();

    public abstract int perpage();

    public abstract int total();

    public abstract List<FlickrPhoto> photo();

    public static FlickrPhotos create(int page, int pages, int perpage, int total,
                                      List<FlickrPhoto> photo) {
        return new AutoValue_FlickrPhotos(page, pages, perpage, total, photo);
    }
}
