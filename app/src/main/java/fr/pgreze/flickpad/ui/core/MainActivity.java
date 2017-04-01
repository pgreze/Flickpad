package fr.pgreze.flickpad.ui.core;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import fr.pgreze.flickpad.R;
import fr.pgreze.flickpad.app.FlickPadApp;
import fr.pgreze.flickpad.common.di.HasComponent;
import fr.pgreze.flickpad.ui.core.di.ActivityComponent;
import fr.pgreze.flickpad.ui.core.di.ActivityModule;
import fr.pgreze.flickpad.ui.core.di.DaggerActivityComponent;
import fr.pgreze.flickpad.ui.home.HomeFragment;

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
                    .add(R.id.main_container, new HomeFragment(), HomeFragment.TAG)
                    .commit();
        }
    }

    @Override
    public ActivityComponent component() {
        if (component == null) {
            component = DaggerActivityComponent.builder()
                    .appComponent(FlickPadApp.getInstance().component())
                    .activityModule(new ActivityModule(this))
                    .build();
        }
        return component;
    }
}
