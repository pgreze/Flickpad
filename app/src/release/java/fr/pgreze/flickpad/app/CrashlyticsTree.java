package fr.pgreze.flickpad.app;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import timber.log.Timber;

public class CrashlyticsTree extends Timber.Tree {

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        // Log messages
        if (priority > Log.INFO && priority <= Log.ERROR) {
            Crashlytics.log(priority, tag, message);
        }
        // Log exception
        if (priority == Log.ERROR && t != null) {
            Crashlytics.logException(t);
        }
    }
}
