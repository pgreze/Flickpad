package fr.pgreze.flickpad.ui.core.di;

import dagger.Component;
import fr.pgreze.flickpad.app.AppComponent;
import fr.pgreze.flickpad.common.di.UILifecycleScope;
import fr.pgreze.flickpad.ui.core.MainActivity;
import fr.pgreze.flickpad.ui.home.HomeFragment;
import fr.pgreze.flickpad.ui.home.PhotosFragment;

@UILifecycleScope
@Component(dependencies = AppComponent.class, modules = {
        ActivityModule.class,
})
public interface ActivityComponent {
    void inject(MainActivity activity);
    void inject(HomeFragment fragment);
    void inject(PhotosFragment fragment);
}
