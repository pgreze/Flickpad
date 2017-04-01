package fr.pgreze.flickpad.data.flickr.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * Flickr error representation.
 */
@AutoValue
public abstract class FlickrResponse<T> {

    /** Values: ok, fail */
    public abstract String stat();

    /** Code specific to each request */
    public abstract int code();

    /** Error message */
    public abstract String message();

    /** Flickr response if stat == ok */
    @Nullable
    public abstract T response();

    public static <T> FlickrResponse<T> create(String stat, int code, String message) {
        return new AutoValue_FlickrResponse<>(stat, code, String.valueOf(message), null);
    }

    public static <T> FlickrResponse<T> create(@NonNull String stat, @NonNull T response) {
        return new AutoValue_FlickrResponse<>(stat, 0, "", response);
    }
}
