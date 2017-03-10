package com.github.akvast.weartest.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.github.akvast.weartest.App;

import java.io.File;

public final class Utils {

    private static final String TAG = getTag(Utils.class);

    public static String getTag(Class clazz) {
        return "Parcha:" + clazz.getSimpleName();
    }

    public static int dpToPx(int dp) {
        // Get the screen's density scale
        final float scale = App.sContext.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

    public static File getCacheDir(Context context, String subfolder) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null)
            cacheDir = context.getCacheDir();

        File dir = new File(cacheDir, subfolder);
        dir.mkdirs();

        return dir;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null)
            view = new View(activity);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null)
            inputMethodManager.showSoftInput(view, 0);
    }

}
