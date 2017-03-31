package fr.pgreze.flickpad.ui.core;

import android.support.annotation.Nullable;

/**
 * Base presenter class.
 */
public class BasePresenter<View> {

    @Nullable
    protected View view;
    private boolean paused = true;

    public void onStart(View view) {
        this.view = view;
    }

    public void onResume() {
        paused = false;
    }

    public void onPause() {
        paused = true;
    }

    public void onStop() {
        view = null;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isStopped() {
        return view == null;
    }
}
