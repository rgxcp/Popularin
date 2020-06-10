package xyz.fairportstudios.popularin.apis.popularin.get;

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
import xyz.fairportstudios.popularin.statics.PopularinAPI;
import xyz.fairportstudios.popularin.models.SelfDetail;
import xyz.fairportstudios.popularin.preferences.Auth;

public class SelfDetailRequest {
    private Context context;

    public SelfDetailRequest(Context context) {
        this.context = context;
    }

    public interface Callback {
        void onSuccess(SelfDetail selfDetail);

        void onError(String message);
    }

    public void sendRequest(final Callback callback) {
        String requestURL = PopularinAPI.SELF;

        JsonObjectRequest selfDetail = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject resultObject = response.getJSONObject("result");

                        SelfDetail selfDetail = new SelfDetail();
                        selfDetail.setFull_name(resultObject.getString("full_name"));
                        selfDetail.setUsername(resultObject.getString("username"));
                        selfDetail.setEmail(resultObject.getString("email"));
                        callback.onSuccess(selfDetail);
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
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("API-Key", PopularinAPI.API_KEY);
                headers.put("Auth-Token", new Auth(context).getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(selfDetail);
    }
}
