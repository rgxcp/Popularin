package xyz.fairportstudios.popularin.apis.popularin.delete;

import android.content.Context;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.secrets.APIKey;
import xyz.fairportstudios.popularin.statics.PopularinAPI;
import xyz.fairportstudios.popularin.preferences.Auth;

public class UnfollowUserRequest {
    private Context mContext;
    private int mUserID;

    public UnfollowUserRequest(Context context, int userID) {
        mContext = context;
        mUserID = userID;
    }

    public interface Callback {
        void onSuccess();

        void onError(String message);
    }

    public void sendRequest(final Callback callback) {
        String requestURL = PopularinAPI.USER + mUserID + "/unfollow";

        JsonObjectRequest unfollowUser = new JsonObjectRequest(Request.Method.DELETE, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 404) {
                        callback.onSuccess();
                    } else {
                        callback.onError(mContext.getString(R.string.general_error));
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                    callback.onError(mContext.getString(R.string.general_error));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof NetworkError || error instanceof TimeoutError) {
                    callback.onError(mContext.getString(R.string.network_error));
                } else if (error instanceof ServerError) {
                    callback.onError(mContext.getString(R.string.server_error));
                } else {
                    callback.onError(mContext.getString(R.string.general_error));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("API-Key", APIKey.POPULARIN_API_KEY);
                headers.put("Auth-Token", new Auth(mContext).getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(mContext).add(unfollowUser);
    }
}
