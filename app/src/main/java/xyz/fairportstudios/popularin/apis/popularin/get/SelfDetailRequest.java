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
import xyz.fairportstudios.popularin.models.SelfDetail;
import xyz.fairportstudios.popularin.preferences.Auth;

public class SelfDetailRequest {
    private Context context;

    public SelfDetailRequest(Context context) {
        this.context = context;
    }

    public interface APICallback {
        void onSuccess(SelfDetail selfDetail);

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        final Auth auth = new Auth(context);

        String requestURL = PopularinAPI.USER_SELF;

        JsonObjectRequest selfDetail = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject result = response.getJSONObject("result");

                        SelfDetail selfDetail = new SelfDetail();
                        selfDetail.setFirst_name(result.getString("first_name"));
                        selfDetail.setLast_name(result.getString("last_name"));
                        selfDetail.setUsername(result.getString("username"));
                        selfDetail.setEmail(result.getString("email"));
                        callback.onSuccess(selfDetail);
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
                headers.put("Auth-ID", auth.getAuthID());
                headers.put("Auth-Token", auth.getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(selfDetail);
    }
}