package fr.pgreze.flickpad.app;

import com.facebook.stetho.Stetho;

import timber.log.Timber;

/**
 * Debug config
 */
class BuildVariant {

    static void config(FlickPadApp app) {
        Stetho.initializeWithDefaults(app);

        // Logging config
        Timber.plant(new Timber.DebugTree());
        // See https://gist.github.com/AlbertVilaCalvo/ae814bcc61de8205b8967feddd0d4faa
        Timber.plant(new StethoDebugTree());
    }
}
