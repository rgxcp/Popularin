package xyz.fairportstudios.popularin.apis.popularin;

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

    public void sendRequest() {
        String requestURL = PopularinBaseRequest.REVIEW;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArrayData = response.getJSONObject("result").getJSONArray("data");

                    for (int index = 0; index < jsonArrayData.length(); index++) {
                        JSONObject jsonObject = jsonArrayData.getJSONObject(index);

                        Double rating = jsonObject.getDouble("rating");
                        Integer id = jsonObject.getInt("id");
                        Integer tmdb_id = jsonObject.getJSONObject("film").getInt("tmdb_id");
                        Integer user_id = jsonObject.getJSONObject("user").getInt("id");
                        String first_name = jsonObject.getJSONObject("user").getString("first_name");
                        String poster = jsonObject.getJSONObject("film").getString("poster");
                        String profile_picture = jsonObject.getJSONObject("user").getString("profile_picture");
                        String release_date = jsonObject.getJSONObject("film").getString("release_date");
                        String review_text = jsonObject.getString("review_text");
                        String title = jsonObject.getJSONObject("film").getString("title");

                        Review review = new Review(rating, id, tmdb_id, user_id, first_name, poster, profile_picture, release_date, review_text, title);
                        review.setRating(rating);
                        review.setId(id);
                        review.setTmdb_id(tmdb_id);
                        review.setUser_id(user_id);
                        review.setFirst_name(first_name);
                        review.setPoster(poster);
                        review.setProfile_picture(profile_picture);
                        review.setRelease_date(release_date);
                        review.setReview_text(review_text);
                        review.setTitle(title);

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
