package fr.pgreze.flickpad.ui.home;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.pgreze.flickpad.R;
import fr.pgreze.flickpad.common.TextUtils;
import fr.pgreze.flickpad.ui.core.BaseFragment;
import fr.pgreze.flickpad.ui.core.MainActivity;
import fr.pgreze.flickpad.ui.core.ViewHelper;
import fr.pgreze.flickpad.ui.core.di.ActivityComponent;
import fr.pgreze.flickpad.ui.group.GroupsFragment;
import fr.pgreze.flickpad.ui.login.LoginFragment;
import fr.pgreze.flickpad.ui.photo.PhotosFragment;
import fr.pgreze.flickpad.ui.photo.PhotosRequest;

public class HomeFragment extends BaseFragment<HomePresenter> implements HomePresenter.HomeView {
    public static final String TAG = "fragment.home";
    public static final String[] HOME_TAGS = new String[] {"cat", "printemps", "bridge"};

    private static final String SEARCH_KEY = "home.search";

    public static HomeFragment newInstance(@Nullable String search) {
        Bundle args = new Bundle();
        args.putString(SEARCH_KEY, search);

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs_view_pager) ViewPager viewPager;
    @BindView(R.id.tabs_layout) TabLayout tabLayout;

    @Inject MainActivity activity;
    @Inject HomePresenter presenter;

    private HomePagerAdapter pagerAdapter;

    @Nullable
    @Override
    protected HomePresenter onCreate(@NonNull ActivityComponent component,
                                     Bundle args,
                                     @Nullable Bundle savedInstanceState) {
        // Inject
        component.inject(this);

        // Init presenter
        presenter.setArgs(args.getString(SEARCH_KEY, null));
        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        // Configure toolbar
        activity.setSupportActionBar(toolbar);
        if (TextUtils.isEmpty(presenter.getSearchQuery())) {
            toolbar.setTitle(R.string.home_title);
        } else {
            toolbar.setTitle(presenter.getSearchQuery());
            // Add navigation icon
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24);
            toolbar.setNavigationOnClickListener(v -> activity.onBackPressed());
        }
        setHasOptionsMenu(true);

        // Configure pager adapter
        pagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);

        // Listen search request
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                boolean consume = submitSearch(query);
                if (consume) searchView.onActionViewCollapsed();
                return consume;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Set search view hint
        SearchView.SearchAutoComplete searchAutoComplete =
                (SearchView.SearchAutoComplete) searchView.findViewById(
                        android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHint(R.string.search_hint);
    }

    @Override
    public void updateSearch(@NonNull String query) {
        getArguments().putString(SEARCH_KEY, query);
        toolbar.setTitle(query);
    }

    @Override
    public void goToSearch(@NonNull String query) {
        activity.search(query);
    }

    @Override
    public void showUserPhotos() {
        pagerAdapter.notifyDataSetChanged();
    }

    private boolean submitSearch(String query) {
        // Close keyboard
        ViewHelper.closeKeyboard(activity);
        // Handle via presenter
        return presenter.onNewSearch(query);
    }

    /**
     * TODO: avoid to recreate everything with FragmentStatePagerAdapter,
     * like FragmentPagerAdapter but with data set update support.
     */
    private class HomePagerAdapter extends FragmentStatePagerAdapter {

        private final String[] homeTitles;
        private final String[] searchTitles;

        HomePagerAdapter(FragmentManager fm) {
            super(fm);
            Resources r = activity.getResources();
            homeTitles = r.getStringArray(R.array.home_titles);
            searchTitles = r.getStringArray(R.array.search_titles);
        }

        @Override
        public Fragment getItem(int position) {
            String searchQuery = presenter.getSearchQuery();
            if (TextUtils.isEmpty(searchQuery)) {
                if (position < getCount() - 1) {
                    // Photos tag
                    return PhotosFragment.newInstance(PhotosRequest.requestTag(HOME_TAGS[position]));
                } else if (presenter.isLogged()) {
                    return PhotosFragment.newInstance(PhotosRequest.requestUser(null));
                } else {
                    return new LoginFragment();
                }
            } else {
                // Search screen
                if (position == 0) {
                    // Photo search screen
                    return PhotosFragment.newInstance(PhotosRequest.requestSearch(searchQuery));
                } else {
                    // Group search screen
                    return GroupsFragment.newInstance(searchQuery);
                }
            }
        }

        @Override
        public int getCount() {
            return TextUtils.isEmpty(presenter.getSearchQuery()) ? HOME_TAGS.length + 1 : 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TextUtils.isEmpty(presenter.getSearchQuery())
                    ? homeTitles[position]
                    : searchTitles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            if (TextUtils.isEmpty(presenter.getSearchQuery())) {
                // Home
                if (object instanceof LoginFragment) {
                    return presenter.isLogged() ? POSITION_NONE : getCount() - 1;
                } else if (object instanceof PhotosFragment
                        && ((PhotosFragment) object).getRequest().type() == PhotosRequest.REQUEST_USER) {
                    return !presenter.isLogged() ? POSITION_NONE : getCount() - 1;
                }
                // Default behavior
                return super.getItemPosition(object);
            }
            // Search
            return super.getItemPosition(object);
        }
    }
}
