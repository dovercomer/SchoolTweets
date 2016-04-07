package com.techhaven.schooltweets.networks;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Oluwayomi on 2/24/2016.
 */
public class SchooltweetsService extends IntentService {
    public SchooltweetsService() {
        super("schooltweets");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    static public class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
