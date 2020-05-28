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
import xyz.fairportstudios.popularin.models.ReviewDetail;
import xyz.fairportstudios.popularin.preferences.Auth;

public class ReviewDetailRequest {
    private Context context;
    private String id;

    public ReviewDetailRequest(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    public interface APICallback {
        void onSuccess(ReviewDetail reviewDetail);

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        String requestURL = PopularinAPI.REVIEW + "/" + id;

        JsonObjectRequest reviewDetail = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONObject reviewObject = resultObject.getJSONObject("review");
                        JSONObject metadataObject = resultObject.getJSONObject("metadata");
                        JSONObject filmObject = reviewObject.getJSONObject("film");
                        JSONObject userObject = reviewObject.getJSONObject("user");

                        ReviewDetail reviewDetail = new ReviewDetail();
                        reviewDetail.setTmdb_id(filmObject.getInt("tmdb_id"));
                        reviewDetail.setUser_id(userObject.getInt("id"));
                        reviewDetail.setTotal_like(metadataObject.getInt("total_like"));
                        reviewDetail.setIs_liked(metadataObject.getBoolean("is_liked"));
                        reviewDetail.setRating(reviewObject.getDouble("rating"));
                        reviewDetail.setReview_detail(reviewObject.getString("review_detail"));
                        reviewDetail.setReview_date(reviewObject.getString("review_date"));
                        reviewDetail.setWatch_date(reviewObject.getString("watch_date"));
                        reviewDetail.setTitle(filmObject.getString("title"));
                        reviewDetail.setRelease_date(filmObject.getString("release_date"));
                        reviewDetail.setPoster(filmObject.getString("poster"));
                        reviewDetail.setFull_name(userObject.getString("full_name"));
                        reviewDetail.setProfile_picture(userObject.getString("profile_picture"));
                        callback.onSuccess(reviewDetail);
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
                headers.put("Auth-ID", new Auth(context).getAuthID());
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(reviewDetail);
    }
}
