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

public class UpdatePasswordRequest {
    private Context context;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    public UpdatePasswordRequest(
            Context context,
            String currentPassword,
            String newPassword,
            String confirmPassword
    ) {
        this.context = context;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public interface APICallback {
        void onSuccess();

        void onInvalid();

        void onFailed(String message);

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        final Auth auth = new Auth(context);

        String requestURL = PopularinAPI.USER
                + "/"
                + auth.getAuthID()
                + "/password";

        StringRequest updatePassword = new StringRequest(Request.Method.PUT, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObject = new JSONObject(response);
                    int status = responseObject.getInt("status");

                    if (status == 303) {
                        JSONObject resultObject = responseObject.getJSONObject("result");
                        String uid = String.valueOf(resultObject.getInt("id"));
                        String token = resultObject.getString("token");
                        auth.setAuth(uid, token);
                        callback.onSuccess();
                    } else if (status == 616) {
                        callback.onInvalid();
                    } else if (status == 626) {
                        JSONArray resultArray = responseObject.getJSONArray("result");
                        String message = resultArray.get(0).toString();
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
                params.put("current_password", currentPassword);
                params.put("new_password", newPassword);
                params.put("confirm_password", confirmPassword);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(updatePassword);
    }
}
