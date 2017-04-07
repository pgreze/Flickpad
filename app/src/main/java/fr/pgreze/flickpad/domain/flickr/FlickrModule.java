package fr.pgreze.flickpad.domain.flickr;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fr.pgreze.flickpad.BuildConfig;
import fr.pgreze.flickpad.common.model.AutoValueTypeAdapterFactory;
import fr.pgreze.flickpad.data.flickr.FlickrService;
import fr.pgreze.flickpad.data.flickr.model.FlickrResponse;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthProvider;

@Module
public class FlickrModule {

    // Login

    public static final String REQUEST_TOKEN_ENDPOINT_URL = "https://www.flickr.com/services/oauth/request_token";
    public static final String ACCESS_TOKEN_ENDPOINT_URL = "https://www.flickr.com/services/oauth/access_token";
    public static final String AUTHORIZATION_WEBSITE_URL = "https://www.flickr.com/services/oauth/authorize";
    public static final String CALLBACK_URL = "flickpad://oauth-callback";

    @Singleton @Provides OkHttpOAuthProvider provideOAuthProvider(OkHttpClient httpClient) {
        return new OkHttpOAuthProvider(
                REQUEST_TOKEN_ENDPOINT_URL,
                ACCESS_TOKEN_ENDPOINT_URL,
                AUTHORIZATION_WEBSITE_URL,
                httpClient);
    }

    @Singleton @Provides OkHttpOAuthConsumer provideOAuthConsumer() {
        return new OkHttpOAuthConsumer(BuildConfig.FLICKR_CONSUMER_KEY,
                BuildConfig.FLICKR_CONSUMER_SECRET);
    }

    // API

    /** 10 MB */
    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;

    @Provides Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(FlickrResponse.class, new FlickrResponseDeserializer())
                .registerTypeAdapterFactory(new AutoValueTypeAdapterFactory())
                .create();
    }

    @Provides Cache provideCache(Context context) {
        return new Cache(context.getCacheDir(), HTTP_RESPONSE_DISK_CACHE_MAX_SIZE);
    }

    @Provides FlickrService flickrService(Cache cache, OkHttpClient httpClient, Gson gson,
                                          OkHttpOAuthConsumer consumer) {
        // Create client with cache and login support
        OkHttpClient flickrHttpClient = httpClient.newBuilder()
                .cache(cache)
                //TODO: .addInterceptor(new SigningInterceptor(consumer))
                .build();
        // Create retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FlickrService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(flickrHttpClient)
                .build();
        // Return service
        return retrofit.create(FlickrService.class);
    }
}
