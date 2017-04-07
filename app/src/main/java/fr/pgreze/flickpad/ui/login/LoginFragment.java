package fr.pgreze.flickpad.ui.login;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.pgreze.flickpad.R;
import fr.pgreze.flickpad.ui.core.BaseFragment;
import fr.pgreze.flickpad.ui.core.di.ActivityComponent;
import timber.log.Timber;

public class LoginFragment extends BaseFragment<LoginPresenter> implements LoginPresenter.LoginView {
    public static final String TAG = "fragment.login";

    @Inject LoginPresenter presenter;

    private AlertDialog dialog;
    private WebView webview;

    @Nullable
    @Override
    protected LoginPresenter onCreate(@NonNull ActivityComponent component,
                                      @Nullable Bundle arguments,
                                      @Nullable Bundle savedInstanceState) {
        component.inject(this);
        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.login_btn)
    public void onLoginClick() {
        presenter.onLoginClick();
    }

    @Override
    public void showLoading() {
        showDialog();
    }

    @Override
    public void showExternalLogin(String authorizationUrl, String callbackUrl) {
        // Ensure dialog
        if (dialog == null) {
            showLoading();
            dialog.setCancelable(true);
        }
        // Display login page in dialog
        webview.loadUrl(authorizationUrl);
    }

    @Override
    public void displayHome() {
        Snackbar.make(getView(), "おめでとう！", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showNetworkError() {
        dismissDialog();
        Snackbar.make(getView(), getString(R.string.network_error), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showUnknownError() {
        dismissDialog();
        Snackbar.make(getView(), getString(R.string.unknown_error), Snackbar.LENGTH_SHORT).show();
    }

    private void showDialog() {
        dismissDialog();
        FragmentActivity activity = getActivity();

        // Inflate and configure dialog
        View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_oauth_login, null);
        webview = (WebView) layout.findViewById(R.id.login_webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            // From http://stackoverflow.com/questions/22830425/flickr-android-api-oauth-is-not-redirecting-to-grant-access-page
            // Why onPageStarted: http://stackoverflow.com/questions/23298290/webview-shouldoverrideurlloading-not-called-for-invalid-links
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Timber.d("Start page " + url);
                // After successful login the redirected url will contain a parameter named 'oauth_verifier'
                if (presenter.isValidCallbackUrl(url)) {
                    // Close dialog (should be done in presenter)
                    dismissDialog();
                    // Handle callback url
                    presenter.onLoginCompleted(Uri.parse(url));
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Allow to display progress bar during first page loading
                view.setVisibility(View.VISIBLE);
            }
        });

        // Retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        // Adjust WebView size
        webview.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
        webview.setMinimumHeight((int)(displayRectangle.height() * 0.9f));
        // Container also
        layout.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
        layout.setMinimumHeight((int)(displayRectangle.height() * 0.9f));

        // Create dialog
        AlertDialog alertDialog = new AlertDialog.Builder(layout.getContext())
                .setView(layout)
                .setCancelable(false)
                .create();
        alertDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // Show dialog
        dialog = alertDialog;
        alertDialog.show();
    }

    private void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            webview = null;
            dialog = null;
        }
    }
}
