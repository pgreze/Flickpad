package fr.pgreze.flickpad.ui.photo;

import javax.inject.Inject;

import fr.pgreze.flickpad.domain.flickr.FlickrInteractor;
import fr.pgreze.flickpad.domain.model.Page;
import fr.pgreze.flickpad.domain.model.Photo;
import fr.pgreze.flickpad.ui.home.PagePresenter;
import io.reactivex.Observable;
import timber.log.Timber;

public class PhotosPresenter extends PagePresenter<Photo, PagePresenter.PageView<Photo>> {

    private final FlickrInteractor flickrInteractor;

    private PhotosRequest request;

    @Inject
    public PhotosPresenter(FlickrInteractor flickrInteractor) {
        this.flickrInteractor = flickrInteractor;
    }

    public void setArgs(PhotosRequest request) {
        this.request = request;
    }

    public PhotosRequest getRequest() {
        return request;
    }

    @Override
    protected Observable<Page<Photo>> buildDataRequest(boolean refresh) {
        switch (request.type()) {
            case PhotosRequest.REQUEST_TAG:
                return flickrInteractor.tagPhotos(request.extra());
            case PhotosRequest.REQUEST_SEARCH:
                return flickrInteractor.searchPhotos(request.extra());
            case PhotosRequest.REQUEST_GROUP:
                return flickrInteractor.groupPhotos(request.extra());
            case PhotosRequest.REQUEST_USER:
                return flickrInteractor.userPhotos(request.extra());
        }
        throw new UnsupportedOperationException("Unsupported " + request);
    }

    public void onPhotoClick(int position, Photo photo) {
        Timber.i("Click on photo " + position + ": " + photo + " for " + this);
        if (view != null) view.navigateTo(position, photo);
    }

    @Override
    public String toString() {
        return "PhotosPresenter{request=" + request + "}";
    }

    public boolean onNewQuery(String query) {
        // Only non stopped search screen can handles this request
        if (view == null || request.type() != PhotosRequest.REQUEST_SEARCH) return false;

        // Update field
        request = PhotosRequest.requestSearch(query);
        // Reinit state
        view.showLoadingState();
        // And query again data (without cache)
        onRefresh();
        return true;
    }
}
