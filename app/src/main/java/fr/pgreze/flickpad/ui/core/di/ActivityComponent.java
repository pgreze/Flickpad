package fr.pgreze.flickpad.ui.core.di;

import dagger.Component;
import fr.pgreze.flickpad.app.AppComponent;
import fr.pgreze.flickpad.common.di.UILifecycleScope;
import fr.pgreze.flickpad.ui.core.MainActivity;
import fr.pgreze.flickpad.ui.group.GroupFragment;
import fr.pgreze.flickpad.ui.group.GroupsFragment;
import fr.pgreze.flickpad.ui.home.HomeFragment;
import fr.pgreze.flickpad.ui.login.LoginFragment;
import fr.pgreze.flickpad.ui.photo.PhotosFragment;
import fr.pgreze.flickpad.ui.photo.PhotoFragment;

@UILifecycleScope
@Component(dependencies = AppComponent.class, modules = {
        ActivityModule.class,
})
public interface ActivityComponent {
    void inject(MainActivity activity);
    void inject(HomeFragment fragment);
    void inject(PhotosFragment fragment);
    void inject(PhotoFragment fragment);
    void inject(GroupsFragment fragment);
    void inject(GroupFragment fragment);
    void inject(LoginFragment fragment);
}
