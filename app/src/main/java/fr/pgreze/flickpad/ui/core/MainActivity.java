package fr.pgreze.flickpad.ui.core;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import fr.pgreze.flickpad.R;
import fr.pgreze.flickpad.app.FlickpadApp;
import fr.pgreze.flickpad.common.di.HasComponent;
import fr.pgreze.flickpad.domain.model.Group;
import fr.pgreze.flickpad.domain.model.Photo;
import fr.pgreze.flickpad.domain.model.User;
import fr.pgreze.flickpad.ui.core.di.ActivityComponent;
import fr.pgreze.flickpad.ui.core.di.ActivityModule;
import fr.pgreze.flickpad.ui.core.di.DaggerActivityComponent;
import fr.pgreze.flickpad.ui.group.GroupFragment;
import fr.pgreze.flickpad.ui.home.HomeFragment;
import fr.pgreze.flickpad.ui.photo.PhotoFragment;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements HasComponent<ActivityComponent> {

    @Inject Picasso picasso;

    @BindView(R.id.main_container)
    FrameLayout mainContainer;

    private ActivityComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Inject activity
        component().inject(this);

        if (savedInstanceState == null) {
            // Display fragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, HomeFragment.newInstance(null), HomeFragment.TAG)
                    .commit();
        }
    }

    @Override
    public ActivityComponent component() {
        if (component == null) {
            component = DaggerActivityComponent.builder()
                    .appComponent(FlickpadApp.getInstance().component())
                    .activityModule(new ActivityModule(this))
                    .build();
        }
        return component;
    }

    public void search(String query) {
        Timber.i("Display search screen for " + query);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,
                        R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.main_container, HomeFragment.newInstance(query), HomeFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    public void show(Photo photo, View imageView) {
        Timber.i("Display photo screen for " + photo);
        PhotoFragment fragment = PhotoFragment.newInstance(photo);
        // Configure transition TODO: fixme
        // See https://medium.com/@bherbst/fragment-transitions-with-shared-elements-7c7d71d31cbb
        fragment.setEnterTransition(new Fade()
                .setDuration(300));
        // Or try: TransitionInflater.from(this).inflateTransition(android.R.transition.move)
        fragment.setSharedElementEnterTransition(new DetailsTransition()
                .setDuration(300));
        // Show fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, fragment, PhotoFragment.TAG)
                .addToBackStack(null)
                .addSharedElement(imageView, getString(R.string.photo_transition_name))
                .commit();
    }

    public void show(Group group, View view) {
        Timber.i("Display group screen for " + group);
        GroupFragment fragment = GroupFragment.newInstance(group);
        // Configure transition
        fragment.setEnterTransition(new Slide(Gravity.RIGHT)
                .setDuration(300));
        fragment.setSharedElementEnterTransition(new ChangeBounds()
                .setDuration(300));
        // Show fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment, GroupFragment.TAG)
                .addToBackStack(null)
                .addSharedElement(view.findViewById(R.id.group_item_icon), getString(R.string.group_icon_transition_name))
                .addSharedElement(view.findViewById(R.id.group_item_name), getString(R.string.group_name_transition_name))
                .commit();
    }

    public void show(User user) {
        // TODO
    }

    static class DetailsTransition extends TransitionSet {
        DetailsTransition() {
            setOrdering(ORDERING_TOGETHER);
            addTransition(new ChangeBounds())
                    .addTransition(new ChangeTransform())
                    .addTransition(new ChangeImageTransform());
        }
    }
}
