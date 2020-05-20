package xyz.fairportstudios.popularin.apis.popularin.post;

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
import xyz.fairportstudios.popularin.preferences.Auth;

public class LikeReviewRequest {
    private Context context;
    private String id;

    public LikeReviewRequest(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    public interface APICallback {
        void onSuccess();

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        final Auth auth = new Auth(context);

        String requestURL = PopularinAPI.REVIEW + "/" + id + "/like";

        JsonObjectRequest likeReview = new JsonObjectRequest(Request.Method.POST, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 202) {
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
                headers.put("Auth-ID", auth.getAuthID());
                headers.put("Auth-Token", auth.getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(likeReview);
    }
}
