package xyz.fairportstudios.popularin.apis.popularin.get;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import xyz.fairportstudios.popularin.adapters.ReviewAdapter;
import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
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

    public void sendRequest(final APICallback callback) {
        String requestURL = PopularinAPI.REVIEWS;

        JsonObjectRequest reviewRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int total = response.getJSONObject("result").getInt("total");

                    if (total > 0) {
                        JSONArray jsonArrayData = response.getJSONObject("result").getJSONArray("data");

                        for (int index = 0; index < jsonArrayData.length(); index++) {
                            JSONObject jsonObject = jsonArrayData.getJSONObject(index);
                            JSONObject jsonObjectFilm = jsonObject.getJSONObject("film");
                            JSONObject jsonObjectUser = jsonObject.getJSONObject("user");

                            Review review = new Review();
                            review.setId(jsonObject.getInt("id"));
                            review.setTmdb_id(jsonObjectFilm.getInt("tmdb_id"));
                            review.setUser_id(jsonObjectUser.getInt("id"));
                            review.setRating(jsonObject.getDouble("rating"));
                            review.setFirst_name(jsonObjectUser.getString("first_name"));
                            review.setPoster(jsonObjectFilm.getString("poster"));
                            review.setProfile_picture(jsonObjectUser.getString("profile_picture"));
                            review.setRelease_date(jsonObjectFilm.getString("release_date"));
                            review.setReview_text(jsonObject.getString("review_text"));
                            review.setTitle(jsonObjectFilm.getString("title"));

                            reviewList.add(review);
                        }

                        ReviewAdapter reviewAdapter = new ReviewAdapter(context, reviewList);
                        recyclerView.setAdapter(reviewAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setVisibility(View.VISIBLE);
                        callback.onSuccess();
                    } else {
                        callback.onEmpty();
                    }
                } catch (JSONException error) {
                    error.printStackTrace();
                    callback.onError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onError();
            }
        });

        Volley.newRequestQueue(context).add(reviewRequest);
    }
}
