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

    private static final String TAG_KEY = "photos.tag";
    private static final String SEARCH_KEY = "photos.search";

    public static final int PHOTOS_SPAN_COUNT = 2;
    private PhotosAdapter adapter;

    public static PhotosFragment newTagInstance(@NonNull String tag) {
        Bundle args = new Bundle();
        args.putString(TAG_KEY, tag);

        PhotosFragment fragment = new PhotosFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static PhotosFragment newSearchInstance(@NonNull String search) {
        Bundle args = new Bundle();
        args.putString(SEARCH_KEY, search);

        PhotosFragment fragment = new PhotosFragment();
        fragment.setArguments(args);
        return fragment;
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
        presenter.setArgs(args.getString(TAG_KEY, null), args.getString(SEARCH_KEY, null));

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
    protected void onNewQuery(String query) {
        // Update presenter
        if (presenter.onNewQuery(query)) {
            // And update args
            getArguments().putString(SEARCH_KEY, query);
        }
    }

    @Override
    public void load(Page<Photo> page) {
        super.load(page);
        adapter.addItems(page);
    }

    @Override
    public void show(int position, Photo photo) {
        activity.show(photo, getViewHolderFor(position).itemView);
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
