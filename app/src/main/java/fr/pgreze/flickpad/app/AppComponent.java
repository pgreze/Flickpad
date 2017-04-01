package fr.pgreze.flickpad.app;

import android.content.Context;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;
import fr.pgreze.flickpad.domain.flickr.FlickrInteractor;
import fr.pgreze.flickpad.domain.flickr.FlickrModule;
import okhttp3.OkHttpClient;

/**
 * Root component for the app.
 */
@Singleton
@Component(modules = {
        AppModule.class,
        BuildVariantModule.class,
        FlickrModule.class,
})
public interface AppComponent {
    // Base
    Context context();
    Picasso picasso();
    OkHttpClient client();
    // Domain
    FlickrInteractor flickrInteractor();
}
