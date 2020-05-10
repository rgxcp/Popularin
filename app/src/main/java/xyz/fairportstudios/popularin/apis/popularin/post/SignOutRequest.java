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

public class SignOutRequest {
    private Context context;
    private String id;
    private String token;

    public SignOutRequest(Context context, String id, String token) {
        this.context = context;
        this.id = id;
        this.token = token;
    }

    public interface APICallback {
        void onSuccess();

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        String requestURL = PopularinAPI.SIGN_OUT;

        JsonObjectRequest signOutRequest = new JsonObjectRequest(Request.Method.POST, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 525) {
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
                headers.put("auth_uid", id);
                headers.put("auth_token", token);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(signOutRequest);
    }
}
