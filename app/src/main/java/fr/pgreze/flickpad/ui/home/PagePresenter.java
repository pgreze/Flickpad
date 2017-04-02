package fr.pgreze.flickpad.ui.home;

import android.support.annotation.Nullable;

import java.io.IOException;

import fr.pgreze.flickpad.domain.model.Group;
import fr.pgreze.flickpad.domain.model.Page;
import fr.pgreze.flickpad.domain.model.Photo;
import fr.pgreze.flickpad.domain.model.User;
import fr.pgreze.flickpad.ui.core.BasePresenter;
import io.reactivex.Observable;
import timber.log.Timber;

public abstract class PagePresenter<T, View extends PagePresenter.PageView<T>> extends BasePresenter<View> {

    public interface PageView<T> {
        void load(Page<T> page);
        void show(int position, Photo photo);
        void show(int position, Group group);
        void show(User user);
        void showEmptyMessage();
        void showNetworkError();
        void showUnknownError();
    }

    protected abstract Observable<Page<T>> buildDataRequest(boolean refresh);

    // Implementation

    @Nullable
    private Page<T> currentPage;

    @Override
    public void onStart(View view) {
        super.onStart(view);
        // When screen is visible, load data
        Timber.i("Load data for " + this);
        disposables.add(buildDataRequest(false)
                .subscribe(this::setCurrentPage, this::handleError));
    }

    public void onRefresh() {
        Timber.i("Refresh data for " + this);
        disposables.add(buildDataRequest(true)
                .subscribe(this::setCurrentPage, this::handleError));
    }

    @SuppressWarnings({"NullableProblems", "unchecked"})
    protected void setCurrentPage(Page<T> currentPage) {
        this.currentPage = currentPage;
        Timber.i("Received data for " + this);

        if (view != null) {
            if (currentPage.items().isEmpty()) {
                Timber.i("Show empty message");
                view.showEmptyMessage();
            } else {
                Timber.i("Show received data");
                view.load(currentPage);
            }
        }
    }

    protected void handleError(Throwable e) {
        if (view == null) return;

        if (e instanceof IOException) {
            Timber.i("Show network error");
            view.showNetworkError();
        } else {
            Timber.i("Show unknown error");
            view.showUnknownError();
        }
    }
}
