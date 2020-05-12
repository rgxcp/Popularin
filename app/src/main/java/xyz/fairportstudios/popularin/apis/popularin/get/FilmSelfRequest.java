package xyz.fairportstudios.popularin.apis.popularin.get;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.models.FilmSelf;
import xyz.fairportstudios.popularin.preferences.Auth;

public class FilmSelfRequest {
    private Context context;
    private String id;

    public FilmSelfRequest(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    public interface APICallback {
        void onSuccess(FilmSelf filmSelf);

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        final Auth auth = new Auth(context);

        String requestURL = PopularinAPI.FILM + "/" + id + "/self";

        JsonObjectRequest filmSelf = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject jsonObjectResult = response.getJSONObject("result");

                        FilmSelf filmSelf = new FilmSelf();
                        filmSelf.setIn_favorite(jsonObjectResult.getBoolean("in_favorite"));
                        filmSelf.setIn_review(jsonObjectResult.getBoolean("in_review"));
                        filmSelf.setIn_watchlist(jsonObjectResult.getBoolean("in_watchlist"));
                        filmSelf.setLast_rate(jsonObjectResult.getDouble("last_rate"));
                        callback.onSuccess(filmSelf);
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
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("auth_uid", auth.getAuthID());
                headers.put("auth_token", auth.getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(filmSelf);
    }
}