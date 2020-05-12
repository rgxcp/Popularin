package xyz.fairportstudios.popularin.apis.popularin.post;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.preferences.Auth;

public class AddWatchlistRequest {
    private Context context;
    private String id;

    public AddWatchlistRequest(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    public interface APICallback {
        void onSuccess();

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        final Auth auth = new Auth(context);

        String requestURL = PopularinAPI.WATCHLIST;

        StringRequest addWatchlist = new StringRequest(Request.Method.POST, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");

                    if (status == 202) {
                        callback.onSuccess();
                        boolean filmExist = jsonObject.getBoolean("film_exist");

                        if (!filmExist) {
                            addFilmToDatabase();
                        }
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                    callback.onError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onError();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tmdb_id", id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("auth_uid", auth.getAuthID());
                headers.put("auth_token", auth.getAuthToken());
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(addWatchlist);
    }

    private void addFilmToDatabase() {
        //
    }
}
