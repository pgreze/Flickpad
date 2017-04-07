package fr.pgreze.flickpad.ui.home;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Named;

import fr.pgreze.flickpad.common.TextUtils;
import fr.pgreze.flickpad.domain.flickr.FlickrLoginInteractor;
import fr.pgreze.flickpad.ui.core.BasePresenter;
import io.reactivex.subjects.PublishSubject;

public class HomePresenter extends BasePresenter<HomePresenter.HomeView> {

    interface HomeView {
        void updateSearch(@NonNull String query);
        void goToSearch(@NonNull String query);
        void showUserPhotos();
    }

    private final FlickrLoginInteractor flickrLoginInteractor;
    private final PublishSubject<String> searchSubject;

    @Nullable
    private String searchQuery;
    private boolean logged;

    @Inject
    public HomePresenter(FlickrLoginInteractor flickrLoginInteractor,
                         @Named("search") PublishSubject<String> searchSubject) {
        this.flickrLoginInteractor = flickrLoginInteractor;
        this.searchSubject = searchSubject;
    }

    public void setArgs(String searchQuery) {
        this.searchQuery = searchQuery;
        // Resolve logged state
        logged = flickrLoginInteractor.connect();
    }

    @Override
    public void onStart(HomeView homeView) {
        super.onStart(homeView);
        disposables.add(flickrLoginInteractor.getLoginObservable()
                .filter(logged -> logged != this.logged)
                .subscribe(logged -> {
                    if (view != null) view.showUserPhotos();
                }));
    }

    @Nullable
    public String getSearchQuery() {
        return searchQuery;
    }

    public boolean onNewSearch(String query) {
        if (TextUtils.isEmpty(query) || query.equals(searchQuery) || view == null) {
            // Empty or already in this search OR no view
            return false;
        }

        // Clean query
        query = query.trim();

        if (TextUtils.isEmpty(searchQuery)) {
            // Search search screen
            view.goToSearch(query);
        } else {
            // Update state
            searchQuery = query;
            view.updateSearch(query);
            // And display this new search
            searchSubject.onNext(query);
        }
        return true;
    }

    /** @return if user has logged his account */
    public boolean isLogged() {
        return flickrLoginInteractor.connect();
    }
}
