package fr.pgreze.flickpad.ui.core;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import fr.pgreze.flickpad.R;
import fr.pgreze.flickpad.app.FlickPadApp;
import fr.pgreze.flickpad.common.di.HasComponent;
import fr.pgreze.flickpad.ui.core.di.ActivityComponent;
import fr.pgreze.flickpad.ui.core.di.ActivityModule;
import fr.pgreze.flickpad.ui.core.di.DaggerActivityComponent;

public class MainActivity extends AppCompatActivity implements HasComponent<ActivityComponent> {

    @Inject Picasso picasso;

    private ActivityComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Inject activity
        component().inject(this);
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
