package fr.pgreze.flickpad.ui.group;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.pgreze.flickpad.R;
import fr.pgreze.flickpad.domain.model.Group;
import fr.pgreze.flickpad.domain.model.Page;
import fr.pgreze.flickpad.domain.model.Photo;
import fr.pgreze.flickpad.ui.core.di.ActivityComponent;
import fr.pgreze.flickpad.ui.home.PageFragment;
import fr.pgreze.flickpad.ui.photo.PhotosAdapter;

public class GroupFragment extends PageFragment<Photo, GroupPresenter> implements GroupPresenter.GroupView {
    public static final String TAG = "fragment.group";

    private static final int PHOTOS_SPAN_COUNT = 2;
    private static final String GROUP_ARG = "arg.group";

    public static GroupFragment newInstance(Group group) {
        Bundle args = new Bundle();
        args.putParcelable(GROUP_ARG, group);

        GroupFragment fragment = new GroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.group_icon)
    ImageView groupThumbnailView;
    //@BindView(R.id.group_name)
    //TextView groupName;

    @Inject Picasso picasso;
    @Inject GroupPresenter presenter;

    private PhotosAdapter adapter;

    @Nullable
    @Override
    protected GroupPresenter onCreate(@NonNull ActivityComponent component,
                                     Bundle args,
                                     @Nullable Bundle savedInstanceState) {
        // Inject dependencies
        component.inject(this);

        // Parse args
        presenter.setArgs(args.getParcelable(GROUP_ARG));
        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
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
    public void show(Group group) {
        // Configure toolbar
        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setTitle(group.name());

        // Fix group icon half hidden by toolbar
        // See http://stackoverflow.com/a/33145487/5489877
        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            /**
             * verticalOffset changes in diapason
             * from 0 - appBar is fully unwrapped
             * to -appBarLayout's height - appBar is totally collapsed
             * so in example we hide FAB when user folds half of the appBarLayout
             */
            if (appBarLayout1.getHeight() / 1.90 < -verticalOffset) {
                groupThumbnailView.setVisibility(View.GONE);
            } else {
                groupThumbnailView.setVisibility(View.VISIBLE);
            }
        });

        // Show group info
        //groupName.setText(group.name());
        picasso.load(group.thumbnail())
                .transform(new CircleTransform())
                .fit()
                .centerCrop()
                .into(groupThumbnailView);
    }

    @Override
    public void showLoadingState() {
        adapter.clear();
        setPageState(PAGE_LOADING_STATE);
    }

    @Override
    public void load(Page<Photo> page) {
        super.load(page);
        adapter.addItems(page);
    }

    @Override
    protected void onNewQuery(String query) {
        // Nope
    }

    @Override
    public void onRefresh() {
        presenter.onRefresh();
    }

    @Override
    public void showNetworkError() {
        setErrorTxt(getString(R.string.photos_network_error));
    }

    @Override
    public void navigateTo(int position, Photo photo) {
        activity.show(photo, getListView().findViewHolderForAdapterPosition(position).itemView);
    }
}
