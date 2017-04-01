package fr.pgreze.flickpad.ui.core;

import android.support.annotation.Nullable;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Base presenter class.
 */
public class BasePresenter<View> {

    @Nullable
    protected View view;
    private boolean paused = true;

    protected CompositeDisposable disposables = null;

    public void onStart(View view) {
        this.view = view;
        disposables = new CompositeDisposable();
    }

    public void onResume() {
        paused = false;
    }

    public void onPause() {
        paused = true;
    }

    public void onStop() {
        disposables.dispose();
        view = null;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isStopped() {
        return view == null;
    }
}
