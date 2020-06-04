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

import xyz.fairportstudios.popularin.statics.PopularinAPI;
import xyz.fairportstudios.popularin.preferences.Auth;

public class AddReviewRequest {
    private Context context;
    private String filmID;
    private String rating;
    private String review;
    private String date;

    public AddReviewRequest(Context context, String filmID, String rating, String review, String date) {
        this.context = context;
        this.filmID = filmID;
        this.rating = rating;
        this.review = review;
        this.date = date;
    }

    public interface APICallback {
        void onSuccess();

        void onFailed(String message);

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        final Auth auth = new Auth(context);

        String requestURL = PopularinAPI.REVIEW;

        StringRequest addReview = new StringRequest(Request.Method.POST, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObject = new JSONObject(response);
                    int status = responseObject.getInt("status");

                    if (status == 202) {
                        callback.onSuccess();
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
                params.put("tmdb_id", filmID);
                params.put("rating", rating);
                params.put("review_detail", review);
                params.put("watch_date", date);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Auth-ID", auth.getAuthID());
                headers.put("Auth-Token", auth.getAuthToken());
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(addReview);
    }
}
