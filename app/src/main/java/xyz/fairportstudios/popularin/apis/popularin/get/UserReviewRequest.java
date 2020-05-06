package xyz.fairportstudios.popularin.apis.popularin.get;

import android.content.Context;
import android.util.Log;
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

import xyz.fairportstudios.popularin.adapters.UserReviewAdapter;
import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.models.UserReview;

public class UserReviewRequest {
    private String id;
    private Context context;
    private List<UserReview> userReviewList;
    private RecyclerView recyclerView;

    public UserReviewRequest(String id, Context context, List<UserReview> userReviewList, RecyclerView recyclerView) {
        this.id = id;
        this.context = context;
        this.userReviewList = userReviewList;
        this.recyclerView = recyclerView;
    }

    public interface JSONCallback {
        void onSuccess();
        void onEmptyReview();
    }

    public void sendRequest(final JSONCallback callback) {
        String requestURL = PopularinAPI.USER + "/" + id + "/reviews";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONArray jsonArrayData = response.getJSONObject("result").getJSONArray("data");

                        for (int index = 0; index < jsonArrayData.length(); index++) {
                            JSONObject jsonObject = jsonArrayData.getJSONObject(index);
                            JSONObject jsonObjectFilm = jsonObject.getJSONObject("film");

                            Double rating = jsonObject.getDouble("rating");
                            Integer id = jsonObject.getInt("id");
                            Integer filmID = jsonObjectFilm.getInt("tmdb_id");
                            String poster = jsonObjectFilm.getString("poster");
                            String releaseDate = jsonObjectFilm.getString("release_date");
                            String reviewDate = jsonObject.getString("review_date");
                            String reviewText = jsonObject.getString("review_text");
                            String title = jsonObjectFilm.getString("title");

                            UserReview userReview = new UserReview(rating, id, filmID, poster, releaseDate, reviewDate, reviewText, title);
                            userReview.setRating(rating);
                            userReview.setId(id);
                            userReview.setTmdb_id(filmID);
                            userReview.setPoster(poster);
                            userReview.setRelease_date(releaseDate);
                            userReview.setReview_date(reviewDate);
                            userReview.setReview_text(reviewText);
                            userReview.setTitle(title);

                            userReviewList.add(userReview);
                        }

                        UserReviewAdapter userReviewAdapter = new UserReviewAdapter(context, userReviewList);
                        recyclerView.setAdapter(userReviewAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setVisibility(View.VISIBLE);
                        callback.onSuccess();
                    } else {
                        callback.onEmptyReview();
                    }
                } catch (JSONException error) {
                    error.printStackTrace();
                }
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
