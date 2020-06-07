package xyz.fairportstudios.popularin.apis.popularin.delete;

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

import xyz.fairportstudios.popularin.statics.PopularinAPI;
import xyz.fairportstudios.popularin.preferences.Auth;

public class UnlikeReviewRequest {
    private Context context;
    private String id;

    public UnlikeReviewRequest(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    public interface APICallback {
        void onSuccess();

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        String requestURL = PopularinAPI.REVIEW + "/" + id + "/unlike";

        JsonObjectRequest unlikeReview = new JsonObjectRequest(Request.Method.DELETE, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 404) {
                        callback.onSuccess();
                    } else {
                        callback.onError();
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
                headers.put("API-Token", PopularinAPI.API_TOKEN);
                headers.put("Auth-Token", new Auth(context).getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(unlikeReview);
    }
}
