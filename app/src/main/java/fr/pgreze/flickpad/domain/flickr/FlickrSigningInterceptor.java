package fr.pgreze.flickpad.domain.flickr;

import java.io.IOException;

import fr.pgreze.flickpad.BuildConfig;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

/**
 * Signing requests only if we're logged.
 */
public class FlickrSigningInterceptor extends SigningInterceptor {

    private final FlickrLoginInteractor flickrLoginInteractor;

    public FlickrSigningInterceptor(OkHttpOAuthConsumer consumer,
                                    FlickrLoginInteractor flickrLoginInteractor) {
        super(consumer);
        this.flickrLoginInteractor = flickrLoginInteractor;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (flickrLoginInteractor.connect()) {
            // Use OAuth key
            return super.intercept(chain);
        } else {
            // Append App API Key as query parameter
            Request request = chain.request();
            HttpUrl url = request.url().newBuilder()
                    .addQueryParameter("api_key", BuildConfig.FLICKR_CONSUMER_KEY)
                    .build();
            return chain.proceed(request.newBuilder().url(url).build());
        }
    }
}
