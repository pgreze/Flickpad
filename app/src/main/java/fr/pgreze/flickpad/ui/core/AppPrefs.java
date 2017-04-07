package fr.pgreze.flickpad.ui.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import javax.inject.Inject;
import javax.inject.Singleton;

import fr.pgreze.flickpad.common.TextUtils;

/**
 * App preferences
 */
@Singleton
public class AppPrefs {

    private static final String PREFS_NAME = "flickpad";
    private static final String ACCESS_TOKEN = "login.token.key";
    private static final String ACCESS_TOKEN_SECRET = "login.token.secret";

    private final SharedPreferences prefs;

    @Inject
    public AppPrefs(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Nullable
    public Pair<String, String> getAccessToken() {
        String token = prefs.getString(ACCESS_TOKEN, null);
        String tokenSecret = prefs.getString(ACCESS_TOKEN_SECRET, null);
        if (TextUtils.isEmpty(token) || TextUtils.isEmpty(tokenSecret)) {
            return null;
        }
        return Pair.create(token, tokenSecret);
    }

    public void setAccessToken(@Nullable String token, @Nullable String tokenSecret) {
        prefs.edit()
                .putString(ACCESS_TOKEN, token)
                .putString(ACCESS_TOKEN_SECRET, tokenSecret)
                .apply();
    }
}