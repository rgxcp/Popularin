package xyz.fairportstudios.popularin.apis.popularin.get;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.fairportstudios.popularin.adapters.ReviewAdapter;
import xyz.fairportstudios.popularin.statics.PopularinAPI;
import xyz.fairportstudios.popularin.models.Review;

public class ReviewRequest {
    private Context context;
    private List<Review> reviewList;
    private RecyclerView recyclerView;

    public ReviewRequest(Context context, List<Review> reviewList, RecyclerView recyclerView) {
        this.context = context;
        this.reviewList = reviewList;
        this.recyclerView = recyclerView;
    }

    public interface APICallback {
        void onSuccess();

        void onEmpty();

        void onError();
    }

    public String getRequestURL(Integer page) {
        return PopularinAPI.REVIEWS + "?page=" + page;
    }

    public void sendRequest(String requestURL, final APICallback callback) {
        JsonObjectRequest review = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONArray dataArray = resultObject.getJSONArray("data");

                        for (int index = 0; index < dataArray.length(); index++) {
                            JSONObject reviewObject = dataArray.getJSONObject(index);
                            JSONObject filmObject = reviewObject.getJSONObject("film");
                            JSONObject userObject = reviewObject.getJSONObject("user");

                            Review review = new Review();
                            review.setReview_id(reviewObject.getInt("id"));
                            review.setTmdb_id(filmObject.getInt("tmdb_id"));
                            review.setUser_id(userObject.getInt("id"));
                            review.setRating(reviewObject.getDouble("rating"));
                            review.setReview_detail(reviewObject.getString("review_detail"));
                            review.setTimestamp(reviewObject.getString("timestamp"));
                            review.setTitle(filmObject.getString("title"));
                            review.setRelease_date(filmObject.getString("release_date"));
                            review.setPoster(filmObject.getString("poster"));
                            review.setUsername(userObject.getString("username"));
                            review.setProfile_picture(userObject.getString("profile_picture"));
                            reviewList.add(review);
                        }

                        ReviewAdapter reviewAdapter = new ReviewAdapter(context, reviewList);
                        recyclerView.setAdapter(reviewAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setVisibility(View.VISIBLE);
                        callback.onSuccess();
                    } else if (status == 606) {
                        callback.onEmpty();
                    } else {
                        callback.onEmpty();
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
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(review);
    }
}
