package fr.pgreze.flickpad.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.pgreze.flickpad.R;
import fr.pgreze.flickpad.common.TextUtils;
import fr.pgreze.flickpad.ui.core.BaseFragment;
import fr.pgreze.flickpad.ui.core.BasePresenter;
import fr.pgreze.flickpad.ui.core.MainActivity;
import fr.pgreze.flickpad.ui.core.ViewHelper;
import fr.pgreze.flickpad.ui.core.di.ActivityComponent;
import fr.pgreze.flickpad.ui.group.GroupsFragment;
import fr.pgreze.flickpad.ui.photo.PhotosFragment;
import io.reactivex.subjects.PublishSubject;

public class HomeFragment extends BaseFragment {
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
    @Inject @Named("search") PublishSubject<String> searchSubject;

    @Nullable
    private String search;
    private HomePagerAdapter pagerAdapter;

    @Nullable
    @Override
    protected BasePresenter onCreate(@NonNull ActivityComponent component,
                                     Bundle args,
                                     @Nullable Bundle savedInstanceState) {
        // Inject
        component.inject(this);

        // Parse args
        search = args.getString(SEARCH_KEY, null);

        // No presenter
        return null;
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
        if (TextUtils.isEmpty(search)) {
            toolbar.setTitle(R.string.home_title);
        } else {
            toolbar.setTitle(search);
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

    private boolean submitSearch(String query) {
        if (TextUtils.isEmpty(query) || query.equals(search)) {
            // Empty or already in this search
            return false;
        }

        // Clean query
        query = query.trim();

        if (TextUtils.isEmpty(search)) {
            // Search search screen
            activity.search(query);
        } else {
            // Update title and args
            getArguments().putString(SEARCH_KEY, query);
            toolbar.setTitle(query);
            // And display this new search
            searchSubject.onNext(query);
        }

        // Close keyboard
        ViewHelper.closeKeyboard(activity);

        return true;
    }

    private class HomePagerAdapter extends FragmentPagerAdapter {

        HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (TextUtils.isEmpty(search)) {
                // Home screen
                return PhotosFragment.newTagInstance(HOME_TAGS[position]);
            } else if (position == 0) {
                // Photo search screen
                return PhotosFragment.newSearchInstance(search);
            } else {
                // Group search screen
                return GroupsFragment.newInstance(search);
            }
        }

        @Override
        public int getCount() {
            return TextUtils.isEmpty(search) ? HOME_TAGS.length : 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (TextUtils.isEmpty(search)) {
                // Home titles
                switch (position) {
                    case 0:
                        return getString(R.string.home_first_tab_title);
                    case 1:
                        return getString(R.string.home_second_tab_title);
                    default:
                        return getString(R.string.home_third_tab_title);
                }
            } else {
                // Search titles
                switch (position) {
                    case 0:
                        return getString(R.string.search_first_tab_title);
                    default:
                        return getString(R.string.search_second_tab_title);
                }
            }
        }
    }
}
