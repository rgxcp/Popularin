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

import xyz.fairportstudios.popularin.statics.PopularinAPI;
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
        String requestURL = PopularinAPI.USER_SELF;

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
                headers.put("API-Token", PopularinAPI.API_TOKEN);
                headers.put("Auth-Token", new Auth(context).getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(selfDetail);
    }
}
