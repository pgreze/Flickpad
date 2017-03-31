package fr.pgreze.flickpad.app;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class BuildVariantModule {

    @Provides OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }
}
