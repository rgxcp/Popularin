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
import xyz.fairportstudios.popularin.models.ReviewDetail;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.statics.PopularinAPI;

public class ReviewDetailRequest {
    private Context context;
    private Integer id;

    public ReviewDetailRequest(Context context, Integer id) {
        this.context = context;
        this.id = id;
    }

    public interface Callback {
        void onSuccess(ReviewDetail reviewDetail);

        void onError(String message);
    }

    public void sendRequest(final Callback callback) {
        String requestURL = PopularinAPI.REVIEW + id;

        JsonObjectRequest reviewDetail = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONObject filmObject = resultObject.getJSONObject("film");
                        JSONObject userObject = resultObject.getJSONObject("user");

                        ReviewDetail reviewDetail = new ReviewDetail();
                        reviewDetail.setTmdb_id(filmObject.getInt("tmdb_id"));
                        reviewDetail.setUser_id(userObject.getInt("id"));
                        reviewDetail.setTotal_like(resultObject.getInt("total_like"));
                        reviewDetail.setIs_liked(resultObject.getBoolean("is_liked"));
                        reviewDetail.setRating(resultObject.getDouble("rating"));
                        reviewDetail.setReview_detail(resultObject.getString("review_detail"));
                        reviewDetail.setReview_date(resultObject.getString("review_date"));
                        reviewDetail.setWatch_date(resultObject.getString("watch_date"));
                        reviewDetail.setTitle(filmObject.getString("title"));
                        reviewDetail.setRelease_date(filmObject.getString("release_date"));
                        reviewDetail.setPoster(filmObject.getString("poster"));
                        reviewDetail.setUsername(userObject.getString("username"));
                        reviewDetail.setProfile_picture(userObject.getString("profile_picture"));
                        callback.onSuccess(reviewDetail);
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

        Volley.newRequestQueue(context).add(reviewDetail);
    }
}
