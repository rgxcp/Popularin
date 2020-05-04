package xyz.fairportstudios.popularin.apis.popularin.post;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;

public class SignOut {
    private Context context;
    private String id;
    private String token;

    public SignOut(Context context, String id, String token) {
        this.context = context;
        this.id = id;
        this.token = token;
    }

    public interface JSONCallback {
        void onSuccess(JSONObject response);
    }

    public void sendRequest(final JSONCallback callback) {
        String requestURL = PopularinAPI.SIGN_OUT;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    callback.onSuccess(jsonObject);
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
                headers.put("auth_uid", id);
                headers.put("auth_token", token);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
