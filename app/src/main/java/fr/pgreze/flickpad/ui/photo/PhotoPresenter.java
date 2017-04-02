package fr.pgreze.flickpad.ui.photo;

import java.io.File;

import javax.inject.Inject;

import fr.pgreze.flickpad.domain.model.Photo;
import fr.pgreze.flickpad.domain.photo.ImageWriter;
import fr.pgreze.flickpad.ui.core.BasePresenter;
import timber.log.Timber;

class PhotoPresenter extends BasePresenter<PhotoPresenter.PhotoView> {

    interface PhotoView {
        void showImage(String url);
        void toggleFullscreen(boolean fullscreen);
        void share(File file);
        void showShareError();
        void goBack();
    }

    private final ImageWriter imageWriter;

    private Photo photo;

    @Inject PhotoPresenter(ImageWriter imageWriter) {
        this.imageWriter = imageWriter;
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

    public void onShareClick() {
        Timber.i("Start photo file creation");
        disposables.add(imageWriter.write(photo).subscribe(file -> {
            Timber.i("Share image " + file);
            if (view != null) view.share(file);
        }, e -> {
            if (view != null) view.showShareError();
        }));
    }

    void onBackClick() {
        if (view != null) view.goBack();
    }
}
