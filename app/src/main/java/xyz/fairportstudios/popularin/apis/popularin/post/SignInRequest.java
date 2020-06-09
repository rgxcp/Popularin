package xyz.fairportstudios.popularin.apis.popularin.post;

import android.content.Context;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.statics.PopularinAPI;

public class SignInRequest {
    private Context context;
    private String username;
    private String password;

    public SignInRequest(
            Context context,
            String username,
            String password
    ) {
        this.context = context;
        this.username = username;
        this.password = password;
    }

    public interface Callback {
        void onSuccess(Integer id, String token);

        void onUsernameNotFound();

        void onInvalidPassword();

        void onFailed(String message);

        void onError(String message);
    }

    public void sendRequest(final Callback callback) {
        String requestURL = PopularinAPI.SIGN_IN;

        StringRequest signIn = new StringRequest(Request.Method.POST, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObject = new JSONObject(response);
                    int status = responseObject.getInt("status");

                    if (status == 515) {
                        JSONObject resultObject = responseObject.getJSONObject("result");
                        Integer id = resultObject.getInt("id");
                        String token = resultObject.getString("api_token");
                        callback.onSuccess(id, token);
                    } else if (status == 606) {
                        callback.onUsernameNotFound();
                    } else if (status == 616) {
                        callback.onInvalidPassword();
                    } else if (status == 626) {
                        JSONArray resultArray = responseObject.getJSONArray("result");
                        String message = resultArray.get(0).toString();
                        callback.onFailed(message);
                    } else {
                        callback.onError(context.getString(R.string.general_error));
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                    callback.onError(context.getString(R.string.general_error));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof NetworkError || error instanceof TimeoutError) {
                    callback.onError(context.getString(R.string.network_error));
                } else if (error instanceof ServerError) {
                    callback.onError(context.getString(R.string.server_error));
                } else {
                    callback.onError(context.getString(R.string.general_error));
                }
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("API-Key", PopularinAPI.API_KEY);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(signIn);
    }
}
