package xyz.fairportstudios.popularin.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import xyz.fairportstudios.popularin.statics.Popularin;

public class Auth {
    private Context context;

    public Auth(Context context) {
        this.context = context;
    }

    public boolean isAuth() {
        SharedPreferences prefs = context.getSharedPreferences(Popularin.AUTH, Context.MODE_PRIVATE);
        int UID = prefs.getInt(Popularin.AUTH_ID, 0);
        String TOKEN = prefs.getString(Popularin.AUTH_TOKEN, "");

        return UID != 0 && TOKEN != null;
    }

    public int getAuthID() {
        SharedPreferences prefs = context.getSharedPreferences(Popularin.AUTH, Context.MODE_PRIVATE);
        return prefs.getInt(Popularin.AUTH_ID, 0);
    }

    public String getAuthToken() {
        SharedPreferences prefs = context.getSharedPreferences(Popularin.AUTH, Context.MODE_PRIVATE);
        return prefs.getString(Popularin.AUTH_TOKEN, "");
    }

    public void setAuth(Integer id, String token) {
        SharedPreferences prefs = context.getSharedPreferences(Popularin.AUTH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Popularin.AUTH_ID, id);
        editor.putString(Popularin.AUTH_TOKEN, token);
        editor.apply();
    }

    public void delAuth() {
        SharedPreferences prefs = context.getSharedPreferences(Popularin.AUTH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Popularin.AUTH_ID);
        editor.remove(Popularin.AUTH_TOKEN);
        editor.apply();
    }
}
