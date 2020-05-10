package xyz.fairportstudios.popularin.apis.popularin.post;

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

public class SignUpRequest {
    private Context context;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;

    public SignUpRequest(Context context, String firstName, String lastName, String username, String email, String password) {
        this.context = context;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public interface APICallback {
        void onSuccess(String id, String token);

        void onFailed(String message);

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        String requestURL = PopularinAPI.SIGN_UP;

        StringRequest singUpRequest = new StringRequest(Request.Method.POST, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");

                    if (status == 505) {
                        JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
                        String id = String.valueOf(jsonObjectResult.getInt("id"));
                        String token = jsonObjectResult.getString("token");
                        callback.onSuccess(id, token);
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
                params.put("password", password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(singUpRequest);
    }
}
