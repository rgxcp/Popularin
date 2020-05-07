package xyz.fairportstudios.popularin.apis.popularin.get;

import android.content.Context;

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

public class RetrieveReviews {
    private Context context;
    private List<Review> reviewList;
    private RecyclerView recyclerView;

    public RetrieveReviews(Context context, List<Review> reviewList, RecyclerView recyclerView) {
        this.context = context;
        this.reviewList = reviewList;
        this.recyclerView = recyclerView;
    }

    public void sendRequest() {
        String requestURL = PopularinAPI.REVIEWS;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArrayData = response.getJSONObject("result").getJSONArray("data");

                    for (int index = 0; index < jsonArrayData.length(); index++) {
                        JSONObject jsonObject = jsonArrayData.getJSONObject(index);

                        Review review = new Review();
                        review.setId(jsonObject.getInt("id"));
                        review.setTmdb_id(jsonObject.getJSONObject("film").getInt("tmdb_id"));
                        review.setUser_id(jsonObject.getJSONObject("user").getInt("id"));
                        review.setRating(jsonObject.getDouble("rating"));
                        review.setFirst_name(jsonObject.getJSONObject("user").getString("first_name"));
                        review.setPoster(jsonObject.getJSONObject("film").getString("poster"));
                        review.setProfile_picture(jsonObject.getJSONObject("user").getString("profile_picture"));
                        review.setRelease_date(jsonObject.getJSONObject("film").getString("release_date"));
                        review.setReview_text(jsonObject.getString("review_text"));
                        review.setTitle(jsonObject.getJSONObject("film").getString("title"));

                        reviewList.add(review);
                    }
                } catch (JSONException error) {
                    error.printStackTrace();
                }

                ReviewAdapter reviewAdapter = new ReviewAdapter(context, reviewList);
                recyclerView.setAdapter(reviewAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }
}
