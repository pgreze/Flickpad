package fr.pgreze.flickpad.ui.group;

import javax.inject.Inject;

import fr.pgreze.flickpad.domain.flickr.FlickrInteractor;
import fr.pgreze.flickpad.domain.model.Group;
import fr.pgreze.flickpad.domain.model.Page;
import fr.pgreze.flickpad.domain.model.Photo;
import fr.pgreze.flickpad.ui.home.PagePresenter;
import io.reactivex.Observable;
import timber.log.Timber;

public class GroupPresenter extends PagePresenter<Photo, GroupPresenter.GroupView> {

    interface GroupView extends PagePresenter.PageView<Photo> {
        void show(Group group);
    }

    private final FlickrInteractor flickrInteractor;

    private Group group;

    @Inject
    public GroupPresenter(FlickrInteractor flickrInteractor) {
        this.flickrInteractor = flickrInteractor;
    }

    void setArgs(Group group) {
        this.group = group;
    }

    @Override
    public void onStart(GroupView view) {
        super.onStart(view);
        view.show(group);
    }

    public void onPhotoClick(int position, Photo photo) {
        Timber.i("Click on photo " + position + ": " + photo + " for " + this);
        if (view != null) view.show(position, photo);
    }

    @Override
    protected Observable<Page<Photo>> buildDataRequest(boolean refresh) {
        return flickrInteractor.groupPhotos(group.id());
    }
}
