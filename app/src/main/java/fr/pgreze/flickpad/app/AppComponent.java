package fr.pgreze.flickpad.app;

import android.content.Context;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * Root component for the app.
 */
@Singleton
@Component(modules = {
        AppModule.class,
        BuildVariantModule.class,
})
public interface AppComponent {
    // Base
    Context context();
    Picasso picasso();
    OkHttpClient client();
    // Domain
    // TODO
}
