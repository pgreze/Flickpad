package fr.pgreze.flickpad.ui.core.di;

import android.app.Activity;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import fr.pgreze.flickpad.common.di.UILifecycleScope;
import fr.pgreze.flickpad.ui.core.MainActivity;
import io.reactivex.subjects.PublishSubject;

@UILifecycleScope
@Module
public class ActivityModule {

    private final MainActivity activity;

    @Inject
    public ActivityModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides Activity provideActivity() {
        return activity;
    }

    @Provides MainActivity provideMainActivity() {
        return activity;
    }

    @UILifecycleScope @Named("search") @Provides PublishSubject<String> provideSearchSubject() {
        return PublishSubject.create();
    }
}
