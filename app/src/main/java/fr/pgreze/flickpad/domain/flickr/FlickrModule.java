package fr.pgreze.flickpad.domain.flickr;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import fr.pgreze.flickpad.common.model.AutoValueTypeAdapterFactory;
import fr.pgreze.flickpad.data.flickr.FlickrService;
import fr.pgreze.flickpad.data.flickr.model.FlickrResponse;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class FlickrModule {

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

    @Provides FlickrService flickrService(Cache cache, OkHttpClient httpClient, Gson gson) {
        // Create client with cache
        OkHttpClient cachedClient = httpClient.newBuilder()
                .cache(cache)
                .build();
        // Create retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FlickrService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(cachedClient)
                .build();
        // Return service
        return retrofit.create(FlickrService.class);
    }
}
