package xyz.fairportstudios.popularin.apis.popularin.get;

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
import xyz.fairportstudios.popularin.models.UserSelf;
import xyz.fairportstudios.popularin.preferences.Auth;

public class UserSelfRequest {
    private Context context;

    public UserSelfRequest(Context context) {
        this.context = context;
    }

    public interface APICallback {
        void onSuccess(UserSelf userSelf);

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        final Auth auth = new Auth(context);

        String requestURL = PopularinAPI.USER_SELF;

        JsonObjectRequest userSelf = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObjectResult = response.getJSONObject("result");

                    UserSelf userSelf = new UserSelf();
                    userSelf.setFirst_name(jsonObjectResult.getString("first_name"));
                    userSelf.setLast_name(jsonObjectResult.getString("last_name"));
                    userSelf.setUsername(jsonObjectResult.getString("username"));
                    userSelf.setEmail(jsonObjectResult.getString("email"));
                    callback.onSuccess(userSelf);
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
                headers.put("auth_uid", auth.getAuthID());
                headers.put("auth_token", auth.getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(userSelf);
    }
}
