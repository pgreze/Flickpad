package fr.pgreze.flickpad.ui.core;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import timber.log.Timber;

/**
 * Utils methods related to views.
 */
public class ViewHelper {

    public static void closeKeyboard(MainActivity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            try {
                InputMethodManager imm = (InputMethodManager)
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (Exception e) {
                Timber.e(e, "Failed to close keyboard after search");
            }
        }
    }
}
