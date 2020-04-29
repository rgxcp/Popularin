package xyz.fairportstudios.popularin.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class Auth {
    private Context context;

    public Auth(Context context) {
        this.context = context;
    }

    public boolean isAuth() {
        SharedPreferences prefs = context.getSharedPreferences("AUTH", Context.MODE_PRIVATE);
        String UID = prefs.getString("UID", "");
        String TOKEN = prefs.getString("TOKEN", "");

        return !UID.isEmpty() || !TOKEN.isEmpty();
    }

    public String getAuthID() {
        SharedPreferences prefs = context.getSharedPreferences("AUTH", Context.MODE_PRIVATE);
        return prefs.getString("UID", "");
    }

    public String getAuthToken() {
        SharedPreferences prefs = context.getSharedPreferences("AUTH", Context.MODE_PRIVATE);
        return prefs.getString("TOKEN", "");
    }

    public void setAuth(String id, String token) {
        SharedPreferences prefs = context.getSharedPreferences("AUTH", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("UID", id);
        editor.putString("TOKEN", token);
        editor.apply();
    }

    public void delAuth() {
        SharedPreferences prefs = context.getSharedPreferences("AUTH", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("UID");
        editor.remove("TOKEN");
        editor.apply();
    }
}
