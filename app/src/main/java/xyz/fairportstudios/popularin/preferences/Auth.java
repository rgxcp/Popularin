package xyz.fairportstudios.popularin.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import xyz.fairportstudios.popularin.statics.Popularin;

public class Auth {
    private Context mContext;

    public Auth(Context context) {
        mContext = context;
    }

    public boolean isAuth() {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(Popularin.AUTH, Context.MODE_PRIVATE);
        int authID = sharedPrefs.getInt(Popularin.AUTH_ID, 0);
        String authToken = sharedPrefs.getString(Popularin.AUTH_TOKEN, "");
        return authID != 0 && authToken != null;
    }

    public int getAuthID() {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(Popularin.AUTH, Context.MODE_PRIVATE);
        return sharedPrefs.getInt(Popularin.AUTH_ID, 0);
    }

    public String getAuthToken() {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(Popularin.AUTH, Context.MODE_PRIVATE);
        return sharedPrefs.getString(Popularin.AUTH_TOKEN, "");
    }

    public void setAuth(int authID, String authToken) {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(Popularin.AUTH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(Popularin.AUTH_ID, authID);
        editor.putString(Popularin.AUTH_TOKEN, authToken);
        editor.apply();
    }

    public void delAuth() {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(Popularin.AUTH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();
    }
}
