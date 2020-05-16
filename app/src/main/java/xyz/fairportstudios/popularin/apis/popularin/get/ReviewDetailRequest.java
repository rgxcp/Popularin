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
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.services.ParseStar;

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

        JsonObjectRequest reviewDetailRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject jsonObjectResult = response.getJSONObject("result");
                        JSONObject jsonObjectReview = jsonObjectResult.getJSONObject("review");
                        JSONObject jsonObjectMetadata = jsonObjectResult.getJSONObject("metadata");
                        JSONObject jsonObjectFilm = jsonObjectReview.getJSONObject("film");
                        JSONObject jsonObjectUser = jsonObjectReview.getJSONObject("user");

                        ReviewDetail reviewDetail = new ReviewDetail();
                        reviewDetail.setFilm_id(jsonObjectFilm.getInt("tmdb_id"));
                        reviewDetail.setUser_id(jsonObjectUser.getInt("id"));
                        reviewDetail.setLiked(jsonObjectMetadata.getBoolean("liked"));
                        reviewDetail.setRating(jsonObjectReview.getDouble("rating"));
                        reviewDetail.setLikes(jsonObjectMetadata.getInt("likes"));
                        reviewDetail.setPoster(jsonObjectFilm.getString("poster"));
                        reviewDetail.setTitle(jsonObjectFilm.getString("title"));
                        reviewDetail.setRelease_date(jsonObjectFilm.getString("release_date"));
                        reviewDetail.setReview_date(jsonObjectReview.getString("review_date"));
                        reviewDetail.setWatch_date(jsonObjectReview.getString("watch_date"));
                        reviewDetail.setReview_text(jsonObjectReview.getString("review_text"));
                        reviewDetail.setFirst_name(jsonObjectUser.getString("first_name"));
                        reviewDetail.setProfile_picture(jsonObjectUser.getString("profile_picture"));
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
                headers.put("auth_uid", new Auth(context).getAuthID());
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(reviewDetailRequest);
    }
}
