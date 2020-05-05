package xyz.fairportstudios.popularin.apis.popularin.delete;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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

public class UnfollowUser {
    private Context context;
    private String id;

    public UnfollowUser(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    public interface JSONCallback {
        void onSuccess(Integer status);
    }

    public void sendRequest(final JSONCallback callback) {
        String requestURL = PopularinAPI.USER + "/" + id + "/unfollow";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");
                    callback.onSuccess(status);
                } catch (JSONException error) {
                    error.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("auth_uid", new Auth(context).getAuthID());
                headers.put("auth_token", new Auth(context).getAuthToken());
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }
}
