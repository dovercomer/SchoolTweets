package com.techhaven.schooltweets.library;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by Oluwayomi on 3/20/2016.
 */
public class Util {
    public static boolean isValidEmailAddress(CharSequence ad) {
        return !TextUtils.isEmpty(ad) && Patterns.EMAIL_ADDRESS.matcher(ad).matches();
    }
}
