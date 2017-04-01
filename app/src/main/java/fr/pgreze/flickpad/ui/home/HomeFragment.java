package fr.pgreze.flickpad.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fr.pgreze.flickpad.ui.core.BaseFragment;
import fr.pgreze.flickpad.ui.core.BasePresenter;
import fr.pgreze.flickpad.ui.core.di.ActivityComponent;

public class HomeFragment extends BaseFragment {
    public static final String TAG = "fragment.home";

    @Nullable
    @Override
    protected BasePresenter onCreate(@NonNull ActivityComponent component,
                                     @Nullable Bundle args,
                                     @Nullable Bundle savedInstanceState) {
        return null;
    }
}
