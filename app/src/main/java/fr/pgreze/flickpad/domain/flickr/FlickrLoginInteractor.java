package fr.pgreze.flickpad.domain.flickr;

import android.support.v4.util.Pair;

import javax.inject.Inject;
import javax.inject.Singleton;

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

    @Inject
    public FlickrLoginInteractor(OkHttpOAuthProvider oAuthProvider, OkHttpOAuthConsumer consumer) {
        this.oAuthProvider = oAuthProvider;
        this.consumer = consumer;
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
            return Pair.create(consumer.getToken(), consumer.getTokenSecret());
        }).subscribeOn(Schedulers.io())
                .doOnError(e -> Timber.e(e, "Error while fetching access token: " + e));
    }
}
