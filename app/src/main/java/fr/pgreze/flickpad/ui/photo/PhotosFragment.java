package fr.pgreze.flickpad.ui.photo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import javax.inject.Inject;

import fr.pgreze.flickpad.R;
import fr.pgreze.flickpad.domain.model.Page;
import fr.pgreze.flickpad.domain.model.Photo;
import fr.pgreze.flickpad.ui.core.di.ActivityComponent;
import fr.pgreze.flickpad.ui.home.PageFragment;

public class PhotosFragment extends PageFragment<Photo, PhotosPresenter> {

    private static final String REQUEST_KEY = "photos.request";

    public static final int PHOTOS_SPAN_COUNT = 2;
    private PhotosAdapter adapter;

    public static PhotosFragment newInstance(PhotosRequest request) {
        Bundle args = new Bundle();
        args.putParcelable(REQUEST_KEY, request);

        PhotosFragment fragment = new PhotosFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public PhotosRequest getRequest() {
        return getArguments().getParcelable(REQUEST_KEY);
    }

    @Inject PhotosPresenter presenter;

    @Nullable
    @Override
    protected PhotosPresenter onCreate(@NonNull ActivityComponent component,
                                       Bundle args,
                                       @Nullable Bundle savedInstanceState) {
        // Inject fragment
        component.inject(this);

        // Get args
        presenter.setArgs(args.getParcelable(REQUEST_KEY));

        // Return presenter
        return presenter;
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new GridLayoutManager(activity, PHOTOS_SPAN_COUNT);
    }

    @Override
    protected RecyclerView.Adapter createAdapter() {
        adapter = new PhotosAdapter(activity, picasso);
        adapter.setListener(presenter::onPhotoClick);
        return adapter;
    }

    @Override
    public void showLoadingState() {
        adapter.clear();
        setPageState(PAGE_LOADING_STATE);
    }

    @Override
    protected void onNewQuery(String query) {
        // Update presenter
        if (presenter.onNewQuery(query)) {
            // Reset state
            setPageState(PAGE_LOADING_STATE);
            // And update args
            getArguments().putParcelable(REQUEST_KEY, presenter.getRequest());
        }
    }

    @Override
    public void load(Page<Photo> page) {
        super.load(page);
        adapter.addItems(page);
    }

    @Override
    public void navigateTo(int position, Photo photo) {
        activity.show(photo, getListView().findViewHolderForAdapterPosition(position).itemView);
    }

    @Override
    public void onRefresh() {
        presenter.onRefresh();
    }

    @Override
    public void showNetworkError() {
        setErrorTxt(getString(R.string.photos_network_error));
    }
}
