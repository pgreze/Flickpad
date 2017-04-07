package fr.pgreze.flickpad.ui.login;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.IOException;

import javax.inject.Inject;

import fr.pgreze.flickpad.common.TextUtils;
import fr.pgreze.flickpad.common.di.UILifecycleScope;
import fr.pgreze.flickpad.domain.flickr.FlickrLoginInteractor;
import fr.pgreze.flickpad.domain.flickr.FlickrModule;
import fr.pgreze.flickpad.ui.core.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

@UILifecycleScope
public class LoginPresenter extends BasePresenter<LoginPresenter.LoginView> {

    interface LoginView {
        void showLoading();
        void showExternalLogin(String authorizationUrl, String callbackUrl);
        void displayHome();
        void showNetworkError();
        void showUnknownError();
    }

    private final FlickrLoginInteractor loginInteractor;

    private CompositeDisposable disposables;
    private boolean requesting = false;
    private BehaviorSubject<String> requestTokenSubject;

    @Inject
    public LoginPresenter(FlickrLoginInteractor loginInteractor) {
        this.loginInteractor = loginInteractor;
        requestTokenSubject = BehaviorSubject.create();
        disposables = new CompositeDisposable();
    }

    @Override
    public void onStart(LoginView loginView) {
        super.onStart(loginView);
        // Query a new request token if necessary
        ensureRequestToken();
    }

    @Override
    public void onStop() {
        disposables.dispose();
        super.onStop();
    }

    // Login API

    public void setRequestToken(@NonNull String requestToken) {
        // TODO: store token in case of for restart
        requestTokenSubject.onNext(requestToken);
    }

    public void onLoginClick() {
        if (isPaused()) return;

        if (ensureRequestToken()) {
            assert view != null;
            view.showLoading();
        }
        disposables.add(requestTokenSubject
                .take(1)
                .subscribe(authorizationUrl -> {
                    if (view != null) {
                        Timber.i("Display login external view");
                        view.showExternalLogin(authorizationUrl, FlickrModule.CALLBACK_URL);
                    }
                }));
    }

    public boolean isValidCallbackUrl(String url) {
        return url.contains("oauth_verifier=");
    }

    public void onLoginCompleted(Uri url) {
        // Note: no need to check "oauth_token"
        String oauthVerifier = url.getQueryParameter("oauth_verifier");
        if (!TextUtils.isEmpty(oauthVerifier)) {
            disposables.add(loginInteractor.accessToken(oauthVerifier)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(p -> {
                        // Login completed o//
                        if (view != null) {
                            view.displayHome();
                        }
                    }, this::showError));
        } else {
            // Should never occurred
            Timber.e("Empty oauth verifier :o");
            if (view != null) view.showUnknownError();
        }
    }

    // Internal

    private boolean ensureRequestToken() {
        if (!requestTokenSubject.hasValue() && !requesting) {
            requesting = true;
            Timber.i("Request a new request token");
            disposables.add(loginInteractor.requestToken(FlickrModule.CALLBACK_URL)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        Timber.i("Received request token");
                        requestTokenSubject.onNext(s);
                        requesting = false;
                    }, e -> {
                        requesting = false;
                        showError(e);
                    }));
            return true;
        }
        return false;
    }

    private void showError(Throwable e) {
        if (view == null) return;

        if (e instanceof IOException) {
            view.showNetworkError();
        } else {
            view.showUnknownError();
        }
    }
}
