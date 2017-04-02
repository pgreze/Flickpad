package fr.pgreze.flickpad.ui.photo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.pgreze.flickpad.R;
import fr.pgreze.flickpad.domain.model.Photo;
import fr.pgreze.flickpad.ui.core.BaseFragment;
import fr.pgreze.flickpad.ui.core.MainActivity;
import fr.pgreze.flickpad.ui.core.di.ActivityComponent;
import timber.log.Timber;

public class PhotoFragment extends BaseFragment<PhotoPresenter> implements PhotoPresenter.PhotoView {
    public static final String TAG = "fragment.photo";

    private static final String PHOTO_ARG = "photo.item";

    public static PhotoFragment newInstance(Photo photo) {
        Bundle args = new Bundle();
        args.putParcelable(PHOTO_ARG, photo);

        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.photo_toolbar_top)
    Toolbar topToolbar;
    @BindView(R.id.photo_toolbar_down)
    Toolbar downToolbar;
    @BindView(R.id.photo_img)
    ImageView imgView;

    @Inject MainActivity activity;
    @Inject PhotoPresenter presenter;
    @Inject Picasso picasso;

    private boolean fullscreen;

    @Nullable
    @Override
    protected PhotoPresenter onCreate(@NonNull ActivityComponent component,
                                     Bundle args,
                                     @Nullable Bundle savedInstanceState) {
        // Inject fragment
        component.inject(this);

        // Init and return presenter
        presenter.setArgs(args.getParcelable(PHOTO_ARG));
        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        // Top toolbar
        topToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24);
        topToolbar.setNavigationOnClickListener(v -> presenter.onBackClick());

        // Down toolbar
        downToolbar.inflateMenu(R.menu.share);
        downToolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);

        // Img
        imgView.setOnClickListener(v -> presenter.onImageClick(fullscreen));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                presenter.onShareClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showImage(String url) {
        picasso.load(url).into(imgView);
    }

    @Override
    public void toggleFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;

        // Show/Hide system bars TODO: fix me
        /* See http://stackoverflow.com/a/8054262/5489877
        Window window = activity.getWindow();
        WindowManager.LayoutParams attrs = window.getAttributes();
        if (fullscreen) {
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        } else {
            attrs.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        }
        window.setAttributes(attrs);//*/

        // Show/Hide toolbars as well
        int factor = fullscreen ? -1 : 0;
        topToolbar.animate()
                .translationY(factor * topToolbar.getMeasuredHeight())
                .withLayer()
                .start();
        downToolbar.animate()
                .translationY(-factor * downToolbar.getMeasuredHeight())
                .withLayer()
                .start();
    }

    @Override
    public void share(File file) {
        Uri contentUri = FileProvider.getUriForFile(
                getActivity(), getString(R.string.files_authorities), file);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, R.string.share_placeholder_text);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, contentUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(Intent.createChooser(intent, getString(R.string.share_picker_title)));
        } catch (Exception e) {
            Timber.e(e, "Failed to share " + file);
            showShareError();
        }
    }

    @Override
    public void showShareError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, R.string.share_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void goBack() {
        activity.onBackPressed();
    }
}
