package fr.pgreze.flickpad.app;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Root module for the app.
 */
@Module
public class AppModule {

    private final FlickPadApp app;

    public AppModule(FlickPadApp app) {
        this.app = app;
    }

    @Provides Context provideContext() {
        return app;
    }

    @Singleton @Provides Picasso providePicasso(OkHttpClient client) {
        return new Picasso.Builder(app)
                .downloader(new OkHttp3Downloader(client))
                .build();
    }
}
