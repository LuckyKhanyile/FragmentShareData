package co.za.spanbi.fragmentsharedata.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.prefs.PreferencesFactory;


public class SharedPreferenceHelper {
    private static final String PREF_TIME = "Pref time";
    private static SharedPreferenceHelper mInstance;
    SharedPreferences prefs;

    private SharedPreferenceHelper(Context context){
        prefs = context.getSharedPreferences("timeShare", context.MODE_PRIVATE);
    }

    public static SharedPreferenceHelper getmInstance(Context context){
        if(mInstance == null){
            mInstance = new SharedPreferenceHelper(context);
        }
        return mInstance;
    }

    public void saveUpdatedTime(long time){
        prefs.edit().putLong(PREF_TIME, time).apply();
    }

    public long getUpdatedTime(){
        return prefs.getLong(PREF_TIME,0);
    }
}
