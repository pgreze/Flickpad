package fr.pgreze.flickpad.app;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Release config
 */
class BuildVariant {

    static void config(FlickpadApp app) {
        // Configure Crashlytics
        Fabric.with(app, new Crashlytics());
        Timber.plant(new CrashlyticsTree());
    }
}
