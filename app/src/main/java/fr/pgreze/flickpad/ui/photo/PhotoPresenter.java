package fr.pgreze.flickpad.ui.photo;

import javax.inject.Inject;

import fr.pgreze.flickpad.domain.model.Photo;
import fr.pgreze.flickpad.ui.core.BasePresenter;
import timber.log.Timber;

class PhotoPresenter extends BasePresenter<PhotoPresenter.PhotoView> {

    interface PhotoView {
        void showImage(String url);
        void toggleFullscreen(boolean fullscreen);
        void goBack();
    }

    private Photo photo;

    @Inject
    PhotoPresenter() {
        // TODO: analytics
    }

    void setArgs(Photo photo) {
        this.photo = photo;
        if (photo == null) throw new NullPointerException("Empty photo in photo viewer");
    }

    @Override
    public void onStart(PhotoView photoView) {
        super.onStart(photoView);
        Timber.i("Display photo");
        assert view != null;
        view.showImage(photo.largeUrl());
    }

    void onImageClick(boolean fullscreen) {
        if (view != null) view.toggleFullscreen(!fullscreen);
    }

    void onBackClick() {
        if (view != null) view.goBack();
    }

    private boolean isLargeUrl(String url) {
        return url.equals(photo.largeUrl());
    }
}
