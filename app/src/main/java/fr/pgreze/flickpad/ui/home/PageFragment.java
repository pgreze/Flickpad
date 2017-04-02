package fr.pgreze.flickpad.ui.home;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.pgreze.flickpad.R;
import fr.pgreze.flickpad.domain.model.Group;
import fr.pgreze.flickpad.domain.model.Page;
import fr.pgreze.flickpad.domain.model.Photo;
import fr.pgreze.flickpad.domain.model.User;
import fr.pgreze.flickpad.ui.core.BaseFragment;
import fr.pgreze.flickpad.ui.core.BasePresenter;
import fr.pgreze.flickpad.ui.core.MainActivity;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public abstract class PageFragment<T, Presenter extends BasePresenter>
        extends BaseFragment<Presenter>
        implements SwipeRefreshLayout.OnRefreshListener, PagePresenter.PageView<T> {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PAGE_LOADING_STATE, PAGE_DISPLAY_STATE, PAGE_ERROR_STATE})
    public @interface PageState {}
    public static final int PAGE_LOADING_STATE = 0;
    public static final int PAGE_DISPLAY_STATE = 1;
    public static final int PAGE_ERROR_STATE = 2;

    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.list_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.list)
    RecyclerView listView;
    @BindView(R.id.error)
    TextView errorTxt;

    @Inject MainActivity activity;
    @Inject Picasso picasso;
    @Inject @Named("search") PublishSubject<String> searchSubject;

    private Disposable disposable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        // Configure onRefresh layout
        swipeRefreshLayout.setOnRefreshListener(this);
        // TODO: swipeRefreshView.setColorSchemeResources(R.color.primary, R.color.secondary, R.color.third);

        // Configure recycler view
        listView.setHasFixedSize(hasFixedSize());
        listView.setLayoutManager(createLayoutManager());
        listView.setAdapter(createAdapter());
    }

    @Override
    public void onStart() {
        super.onStart();
        disposable = searchSubject.subscribe(this::onNewQuery);
    }

    @Override
    public void onStop() {
        disposable.dispose();
        super.onStop();
    }

    protected abstract RecyclerView.LayoutManager createLayoutManager();

    protected abstract RecyclerView.Adapter createAdapter();

    protected abstract void onNewQuery(String query);

    // Public API

    /**
     * Used for {@link RecyclerView#setHasFixedSize(boolean)}
     * @return if all items have a fixed size
     */
    protected boolean hasFixedSize() {
        return true;
    }

    public void setErrorTxt(CharSequence text) {
        setPageState(PAGE_ERROR_STATE);
        errorTxt.setText(text);
        // Just in case, set onRefresh = false
        swipeRefreshLayout.setRefreshing(false);
    }

    @PageState
    public int getPageState() {
        return errorTxt.getVisibility() == View.VISIBLE ? PAGE_ERROR_STATE
                : swipeRefreshLayout.getVisibility() == View.VISIBLE ? PAGE_DISPLAY_STATE
                : PAGE_LOADING_STATE;
    }

    public void setPageState(@PageState int pageState) {
        // Progress or swipe container
        progressBar.setVisibility(pageState == PAGE_LOADING_STATE ? View.VISIBLE : View.GONE);
        swipeRefreshLayout.setVisibility(pageState != PAGE_LOADING_STATE ? View.VISIBLE : View.GONE);
        // If not progress, set visibility for list or message
        listView.setVisibility(pageState == PAGE_DISPLAY_STATE ? View.VISIBLE : View.GONE);
        errorTxt.setVisibility(pageState == PAGE_ERROR_STATE ? View.VISIBLE : View.GONE);
    }

    // Implementation

    @Override
    public void load(Page<T> page) {
        // Display list if not visible
        setPageState(PAGE_DISPLAY_STATE);
        // And set onRefresh = false
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void show(int position, Photo photo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void show(int position, Group group) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void show(User user) {
        activity.show(user);
    }

    @Override
    public void showEmptyMessage() {
        setErrorTxt(getString(R.string.list_empty));
    }

    @Override
    public void showUnknownError() {
        setErrorTxt(getString(R.string.list_unknown_error));
    }
}
