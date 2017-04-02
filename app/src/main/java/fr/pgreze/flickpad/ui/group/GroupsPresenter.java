package fr.pgreze.flickpad.ui.group;

import javax.inject.Inject;

import fr.pgreze.flickpad.common.di.UILifecycleScope;
import fr.pgreze.flickpad.domain.flickr.FlickrInteractor;
import fr.pgreze.flickpad.domain.model.Group;
import fr.pgreze.flickpad.domain.model.Page;
import fr.pgreze.flickpad.ui.home.PagePresenter;
import io.reactivex.Observable;
import timber.log.Timber;

/** Can be a singleton for this screen, we have only 1 group search screen  */
@UILifecycleScope
class GroupsPresenter extends PagePresenter<Group, PagePresenter.PageView<Group>> {

    private final FlickrInteractor flickrInteractor;

    private String search;

    @Inject
    public GroupsPresenter(FlickrInteractor flickrInteractor) {
        this.flickrInteractor = flickrInteractor;
    }

    void setArgs(String search) {
        this.search = search;
    }

    @Override
    protected Observable<Page<Group>> buildDataRequest(boolean refresh) {
        return flickrInteractor.searchGroups(search);
    }

    public boolean onNewQuery(String query) {
        // Query again data (without cache)
        onRefresh();
        return true;
    }

    void onGroupClick(int position, Group group) {
        Timber.i("Click on group " + position + ": " + group);
        if (view != null) view.navigateTo(position, group);
    }

    @Override
    public String toString() {
        return "GroupsPresenter{search=" + search + "}";
    }
}
