package fr.pgreze.flickpad.ui.home;

import android.support.annotation.Nullable;

import javax.inject.Inject;

import fr.pgreze.flickpad.common.TextUtils;
import fr.pgreze.flickpad.domain.flickr.FlickrInteractor;
import fr.pgreze.flickpad.domain.model.Page;
import fr.pgreze.flickpad.domain.model.Photo;
import io.reactivex.Observable;
import timber.log.Timber;

public class PhotosPresenter extends PagePresenter<Photo, PagePresenter.PageView<Photo>> {

    private final FlickrInteractor flickrInteractor;

    private String tag;
    @Nullable
    private String searchText;

    @Inject
    public PhotosPresenter(FlickrInteractor flickrInteractor) {
        this.flickrInteractor = flickrInteractor;
    }

    public void setArgs(String tag, @Nullable String searchText) {
        this.tag = tag;
        this.searchText = searchText;
    }

    @Override
    protected Observable<Page<Photo>> buildDataRequest(boolean refresh) {
        // Display search if present
        if (TextUtils.isEmpty(searchText)) {
            return flickrInteractor.tagPhotos(tag);
        }
        return flickrInteractor.searchPhotos(searchText);
    }

    public void onPhotoClick(int position, Photo photo) {
        Timber.i("Click on photo " + position + ": " + photo + " for " + this);
        if (view != null) view.show(photo);
    }

    @Override
    public String toString() {
        return "PhotosPresenter{" + (TextUtils.isEmpty(searchText) ? tag : searchText) + "}";
    }

    public boolean onNewQuery(String query) {
        if (TextUtils.isEmpty(searchText)) return false;

        // Update field
        searchText = query;
        // And query again data (without cache)
        onRefresh();
        return true;
    }
}
