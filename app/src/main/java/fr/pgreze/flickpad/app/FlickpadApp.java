package fr.pgreze.flickpad.app;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import fr.pgreze.flickpad.common.CrashlyticsTree;
import fr.pgreze.flickpad.common.di.HasComponent;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class FlickpadApp extends Application implements HasComponent<AppComponent> {

    private static FlickpadApp instance;

    public static FlickpadApp getInstance() {
        return instance;
    }

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        // Base config
        instance = this;
        BuildVariant.config(this);

        // Configure Crashlytics
        Fabric.with(this, new Crashlytics());
        Timber.plant(new CrashlyticsTree());

        component = createComponent();
    }

    @Override
    public AppComponent component() {
        return component;
    }

    protected AppComponent createComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
