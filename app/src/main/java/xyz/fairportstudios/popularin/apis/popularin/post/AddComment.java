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
import xyz.fairportstudios.popularin.preferences.Auth;

public class AddComment {
    private String id;
    private String comment;
    private Context context;

    public AddComment(String id, String comment, Context context) {
        this.id = id;
        this.comment = comment;
        this.context = context;
    }

    public interface JSONCallback {
        void onSuccess(Integer status);
    }

    public void sendRequest(final JSONCallback callback) {
        String requestURL = PopularinAPI.COMMENT;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
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
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("review_id", id);
                params.put("comment_text", comment);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("auth_uid", new Auth(context).getAuthID());
                headers.put("auth_token", new Auth(context).getAuthToken());
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
