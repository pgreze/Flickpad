package fr.pgreze.flickpad.app;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class BuildVariantModule {

    @Provides OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .build();
    }
}
