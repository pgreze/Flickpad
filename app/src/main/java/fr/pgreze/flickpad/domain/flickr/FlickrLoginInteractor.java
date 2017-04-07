package fr.pgreze.flickpad.domain.flickr;

import android.support.v4.util.Pair;

import javax.inject.Inject;
import javax.inject.Singleton;

import fr.pgreze.flickpad.common.TextUtils;
import fr.pgreze.flickpad.ui.core.AppPrefs;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthProvider;
import timber.log.Timber;

@Singleton
public class FlickrLoginInteractor {

    private final OAuthProvider oAuthProvider;
    private final OAuthConsumer consumer;
    private final AppPrefs appPrefs;

    @Inject
    public FlickrLoginInteractor(OkHttpOAuthProvider oAuthProvider, OkHttpOAuthConsumer consumer,
                                 AppPrefs appPrefs) {
        this.oAuthProvider = oAuthProvider;
        this.consumer = consumer;
        this.appPrefs = appPrefs;
    }

    /**
     * Check if we're connected, or could recover previous state
     * @return if we're connected
     */
    public boolean connect() {
        if (TextUtils.isEmpty(consumer.getToken())) {
            // Try to recover
            Pair<String, String> accessToken = appPrefs.getAccessToken();
            if (accessToken != null) {
                Timber.i("Succeed to recover login state");
                consumer.setTokenWithSecret(accessToken.first, accessToken.second);
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * Request a request token, first step of login
     * @param callbackUrl app's callback url used in second step
     * @return ready to use authorization url
     */
    public Observable<String> requestToken(String callbackUrl) {
        return Observable.fromCallable(() ->
                oAuthProvider.retrieveRequestToken(consumer, callbackUrl))
                .subscribeOn(Schedulers.io())
                .doOnError(e -> Timber.e(e, "Error while fetching request token: " + e));
    }

    public Observable<Pair<String, String>> accessToken(String verifier) {
        return Observable.fromCallable(() -> {
            // Request an access token
            oAuthProvider.retrieveAccessToken(consumer, verifier);
            // At this point, consumer as an access token
            String token = consumer.getToken();
            String tokenSecret = consumer.getTokenSecret();
            // Store these credentials
            appPrefs.setAccessToken(token, tokenSecret);
            // And return them
            return Pair.create(token, tokenSecret);
        }).subscribeOn(Schedulers.io())
                .doOnError(e -> Timber.e(e, "Error while fetching access token: " + e));
    }
}
