package fr.pgreze.flickpad.ui.group;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import javax.inject.Inject;

import fr.pgreze.flickpad.R;
import fr.pgreze.flickpad.domain.model.Group;
import fr.pgreze.flickpad.domain.model.Page;
import fr.pgreze.flickpad.ui.core.di.ActivityComponent;
import fr.pgreze.flickpad.ui.home.PageFragment;

public class GroupsFragment extends PageFragment<Group, GroupsPresenter> {
    public static final String TAG = "fragment.groups";
    public static final int GROUPS_SPAN_COUNT = 2;

    private static final String SEARCH_ARG = "group.search";
    private GroupsAdapter adapter;

    public static GroupsFragment newInstance(@NonNull String search) {
        Bundle args = new Bundle();
        args.putString(SEARCH_ARG, search);

        GroupsFragment fragment = new GroupsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject GroupsPresenter presenter;

    @Nullable
    @Override
    protected GroupsPresenter onCreate(@NonNull ActivityComponent component,
                                       Bundle args,
                                       @Nullable Bundle savedInstanceState) {
        component.inject(this);
        presenter.setArgs(args.getString(SEARCH_ARG, null));
        return presenter;
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new GridLayoutManager(activity, GROUPS_SPAN_COUNT);
    }

    @Override
    protected RecyclerView.Adapter createAdapter() {
        adapter = new GroupsAdapter(activity, picasso);
        adapter.setListener(presenter::onGroupClick);
        return adapter;
    }

    @Override
    public void showLoadingState() {
        adapter.clear();
        setPageState(PAGE_LOADING_STATE);
    }

    @Override
    protected void onNewQuery(String query) {
        getArguments().putString(SEARCH_ARG, query);
        presenter.onNewQuery(query);
    }

    @Override
    public void load(Page<Group> page) {
        super.load(page);
        adapter.addItems(page);
    }

    @Override
    public void navigateTo(int position, Group group) {
        activity.show(group, getListView().findViewHolderForAdapterPosition(position).itemView);
    }

    @Override
    public void onRefresh() {
        presenter.onRefresh();
    }

    @Override
    public void showNetworkError() {
        setErrorTxt(getString(R.string.groups_network_error));
    }
}
