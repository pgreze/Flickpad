package fr.pgreze.flickpad.common;

import android.support.annotation.Nullable;

/**
 * Inspired by {@link android.text.TextUtils}.
 * Used in order to avoid Robolectric in unit tests.
 */
public class TextUtils {

    /**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }
}
