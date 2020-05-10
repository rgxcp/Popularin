package xyz.fairportstudios.popularin.apis.popularin.put;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.preferences.Auth;

public class UpdateProfileRequest {
    private Context context;
    private String firstName;
    private String lastName;
    private String username;
    private String email;

    public UpdateProfileRequest(Context context, String firstName, String lastName, String username, String email) {
        this.context = context;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
    }

    public interface APICallback {
        void onSuccess();

        void onFailed(String message);

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        final Auth auth = new Auth(context);

        String requestURL = PopularinAPI.USER + "/" + auth.getAuthID();

        StringRequest updateProfileRequest = new StringRequest(Request.Method.PUT, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");

                    if (status == 303) {
                        callback.onSuccess();
                    } else if (status == 626) {
                        JSONArray jsonArrayResult = jsonObject.getJSONArray("result");
                        String message = jsonArrayResult.get(0).toString();
                        callback.onFailed(message);
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
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("username", username);
                params.put("email", email);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("auth_token", auth.getAuthToken());
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(updateProfileRequest);
    }
}
