package fr.pgreze.flickpad.app;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import fr.pgreze.flickpad.common.di.HasComponent;
import io.fabric.sdk.android.Fabric;

public class FlickPadApp extends Application implements HasComponent<AppComponent> {

    private static FlickPadApp instance;

    public static FlickPadApp getInstance() {
        return instance;
    }

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        BuildVariant.config(this);
        Fabric.with(this, new Crashlytics());
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
