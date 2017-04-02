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
        // Display medium url from cache
        Timber.i("Display medium url");
        assert view != null;
        view.showImage(photo.mediumUrl());
    }

    void onImageClick(boolean fullscreen) {
        if (view != null) view.toggleFullscreen(!fullscreen);
    }

    void onShowImageSucceed(String url) {
        if (view == null) return;

        if (isLargeUrl(url)) {
            Timber.i("Succeed to display large url");
        } else {
            // Medium url from cache loaded, now request network for large url
            Timber.i("Now load large url");
            view.showImage(photo.largeUrl());
        }
    }

    void onShowImageFailed(String url) {
        if (view == null) return;

        if (isLargeUrl(url)) {
            Timber.w("Failed to display large url...");
            // TODO: spinner and wait network
        } else {
            Timber.i("Failed to display cached img, rage quit");
            // TODO: error msg
            view.goBack();
        }
    }

    void onBackClick() {
        if (view != null) view.goBack();
    }

    private boolean isLargeUrl(String url) {
        return url.equals(photo.largeUrl());
    }
}
