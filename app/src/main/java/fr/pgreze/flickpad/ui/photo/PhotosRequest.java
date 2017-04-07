package fr.pgreze.flickpad.ui.photo;

import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Allow to identify a photos request.
 */
@AutoValue
public abstract class PhotosRequest implements Parcelable {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({REQUEST_GROUP, REQUEST_USER, REQUEST_TAG, REQUEST_SEARCH})
    public @interface PhotosRequestType {}
    public static final int REQUEST_GROUP = 1;
    public static final int REQUEST_USER = 2;
    public static final int REQUEST_TAG = 3;
    public static final int REQUEST_SEARCH = 4;

    public static PhotosRequest requestGroup(@NonNull String groupId) {
        return new AutoValue_PhotosRequest(REQUEST_GROUP, groupId);
    }

    public static PhotosRequest requestUser(@Nullable String userId) {
        return new AutoValue_PhotosRequest(REQUEST_USER, userId);
    }

    public static PhotosRequest requestTag(@NonNull String tags) {
        return new AutoValue_PhotosRequest(REQUEST_TAG, tags);
    }

    public static PhotosRequest requestSearch(@NonNull String query) {
        return new AutoValue_PhotosRequest(REQUEST_SEARCH, query);
    }

    // Methods

    @PhotosRequestType
    public abstract int type();

    @Nullable
    public abstract String extra();
}
