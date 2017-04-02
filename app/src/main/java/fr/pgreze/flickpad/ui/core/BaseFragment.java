package fr.pgreze.flickpad.ui.core;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import fr.pgreze.flickpad.common.di.HasComponent;
import fr.pgreze.flickpad.ui.core.di.ActivityComponent;

/**
 * Base class for fragment.
 */
public abstract class BaseFragment<Presenter extends BasePresenter> extends Fragment {

    @Nullable
    private Presenter presenter;

    @Nullable
    protected abstract Presenter onCreate(@NonNull ActivityComponent component,
                                          Bundle args,
                                          @Nullable Bundle savedInstanceState);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get component
        ActivityComponent component = ((HasComponent<ActivityComponent>) getActivity()).component();
        // And request presenter
        presenter = onCreate(component, getArguments(), savedInstanceState);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) presenter.onStart(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.onResume();
    }

    @Override
    public void onPause() {
        if (presenter != null) presenter.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        if (presenter != null) presenter.onStop();
        super.onStop();
    }
}
