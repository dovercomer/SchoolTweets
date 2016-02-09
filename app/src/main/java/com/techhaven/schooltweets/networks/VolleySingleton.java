package com.techhaven.schooltweets.networks;

/**
 * Created by Oluwayomi on 2/9/2016.
 */
import com.android.volley.*;
public class VolleySingleton {
    private static VolleySingleton mInstance = null;
private RequestQueue mRequestQueue;
    public VolleySingleton() {

    }

    public VolleySingleton newInstance() {
        if (mInstance == null) {
            mInstance = new VolleySingleton();
        }
        return mInstance;
    }
}
