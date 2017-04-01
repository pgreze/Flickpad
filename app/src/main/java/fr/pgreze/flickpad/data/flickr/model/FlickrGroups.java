package fr.pgreze.flickpad.data.flickr.model;

import com.google.auto.value.AutoValue;

import java.util.List;

import fr.pgreze.flickpad.common.model.AutoGson;

@AutoValue
@AutoGson(autoValueClass = AutoValue_FlickrGroups.class)
public abstract class FlickrGroups {

    public abstract int page();

    public abstract int pages();

    public abstract int perpage();

    public abstract int total();

    public abstract List<FlickrGroup> group();

    public static FlickrGroups create(int page, int pages, int perpage, int total,
                                      List<FlickrGroup> group) {
        return new AutoValue_FlickrGroups(page, pages, perpage, total, group);
    }
}
