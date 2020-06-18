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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.Review;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.secrets.APIKey;
import xyz.fairportstudios.popularin.statics.Popularin;
import xyz.fairportstudios.popularin.statics.PopularinAPI;

public class ReviewRequest {
    private Context context;

    public ReviewRequest(Context context) {
        this.context = context;
    }

    public interface Callback {
        void onSuccess(Integer totalPage, List<Review> reviewList);

        void onNotFound();

        void onError(String message);
    }

    public void sendRequest(Integer page, final Callback callback) {
        String requestURL = PopularinAPI.REVIEWS + "?page=" + page;

        JsonObjectRequest review = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        List<Review> reviewList = new ArrayList<>();
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONArray dataArray = resultObject.getJSONArray("data");
                        Integer totalPage = resultObject.getInt("last_page");

                        for (int index = 0; index < dataArray.length(); index++) {
                            JSONObject indexObject = dataArray.getJSONObject(index);
                            JSONObject filmObject = indexObject.getJSONObject("film");
                            JSONObject userObject = indexObject.getJSONObject("user");

                            Review review = new Review();
                            review.setId(indexObject.getInt("id"));
                            review.setTmdb_id(filmObject.getInt("tmdb_id"));
                            review.setUser_id(userObject.getInt("id"));
                            review.setTotal_like(indexObject.getInt("total_like"));
                            review.setTotal_comment(indexObject.getInt("total_comment"));
                            review.setIs_liked(indexObject.getBoolean("is_liked"));
                            review.setRating(indexObject.getDouble("rating"));
                            review.setReview_detail(indexObject.getString("review_detail"));
                            review.setTimestamp(indexObject.getString("timestamp"));
                            review.setTitle(filmObject.getString("title"));
                            review.setRelease_date(filmObject.getString("release_date"));
                            review.setPoster(filmObject.getString("poster"));
                            review.setUsername(userObject.getString("username"));
                            review.setProfile_picture(userObject.getString("profile_picture"));
                            reviewList.add(review);
                        }

                        callback.onSuccess(totalPage, reviewList);
                    } else if (status == 606) {
                        callback.onNotFound();
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
                headers.put("API-Key", APIKey.POPULARIN_API_KEY);
                headers.put("Auth-Token", new Auth(context).getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(review);
    }
}
