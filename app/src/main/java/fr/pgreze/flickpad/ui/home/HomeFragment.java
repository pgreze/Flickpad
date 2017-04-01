package fr.pgreze.flickpad.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.pgreze.flickpad.R;
import fr.pgreze.flickpad.common.TextUtils;
import fr.pgreze.flickpad.ui.core.BaseFragment;
import fr.pgreze.flickpad.ui.core.BasePresenter;
import fr.pgreze.flickpad.ui.core.MainActivity;
import fr.pgreze.flickpad.ui.core.di.ActivityComponent;

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

    @Nullable
    private String search;
    private HomePagerAdapter pagerAdapter;

    @Nullable
    @Override
    protected BasePresenter onCreate(@NonNull ActivityComponent component,
                                     @Nullable Bundle args,
                                     @Nullable Bundle savedInstanceState) {
        // Inject
        component.inject(this);

        // Parse args
        if (args != null) {
            search = args.getString(SEARCH_KEY, null);
        }

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
            // Add navigation icon
            // See http://stackoverflow.com/a/26656285/5489877
            ActionBar actionBar = activity.getSupportActionBar();
            assert actionBar != null;
            actionBar.setTitle(search);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Configure pager adapter
        pagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
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
            } else {
                // Search screen
                // TODO: groups
                return PhotosFragment.newSearchInstance(search);
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
