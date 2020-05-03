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

import xyz.fairportstudios.popularin.adapters.LatestFavoriteAdapter;
import xyz.fairportstudios.popularin.adapters.LatestReviewAdapter;
import xyz.fairportstudios.popularin.models.LatestFavorite;
import xyz.fairportstudios.popularin.models.LatestReview;

public class ProfileRequest {
    private Context context;
    private String userID;
    private List<LatestFavorite> latestFavoriteList;
    private List<LatestReview> latestReviewList;
    private RecyclerView recyclerViewLatestFavorite;
    private RecyclerView recyclerViewLatestReview;

    public ProfileRequest(Context context, String userID, List<LatestFavorite> latestFavoriteList, List<LatestReview> latestReviewList, RecyclerView recyclerViewLatestFavorite, RecyclerView recyclerViewLatestReview) {
        this.context = context;
        this.userID = userID;
        this.latestFavoriteList = latestFavoriteList;
        this.latestReviewList = latestReviewList;
        this.recyclerViewLatestFavorite = recyclerViewLatestFavorite;
        this.recyclerViewLatestReview = recyclerViewLatestReview;
    }

    public interface JSONCallback {
        void onSuccess(JSONObject response);
        void onEmptyFavorite(Integer favorite);
        void onEmptyReview(Integer review);
    }

    public void getUserDetail(final JSONCallback callback) {
        String requestURL = PopularinBaseRequest.USER_DETAIL + userID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback.onSuccess(response);

                    JSONObject jsonObjectMetadata = response.getJSONObject("result").getJSONObject("metadata");
                    JSONObject jsonObjectActivity = response.getJSONObject("result").getJSONObject("activity");

                    int favorites = jsonObjectMetadata.getInt("favorites");
                    int reviews = jsonObjectMetadata.getInt("reviews");

                    callback.onEmptyFavorite(favorites);
                    callback.onEmptyReview(reviews);

                    if (favorites >= 1) {
                        JSONArray jsonArrayFavorites = jsonObjectActivity.getJSONArray("favorites");
                        getUserLatestFavorite(jsonArrayFavorites);
                    }

                    if (reviews >= 1) {
                        JSONArray jsonArrayReviews = jsonObjectActivity.getJSONArray("reviews");
                        getUserLatestReview(jsonArrayReviews);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void getUserLatestFavorite(JSONArray jsonArray) {
        try {
            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject jsonObject = jsonArray.getJSONObject(index);

                Integer tmdb_id = jsonObject.getJSONObject("film").getInt("tmdb_id");
                String poster = jsonObject.getJSONObject("film").getString("poster");

                LatestFavorite latestFavorite = new LatestFavorite(tmdb_id, poster);
                latestFavorite.setTmdb_id(tmdb_id);
                latestFavorite.setPoster(poster);
                latestFavoriteList.add(latestFavorite);
            }
        } catch (JSONException error) {
            error.printStackTrace();
        }

        LatestFavoriteAdapter latestFavoriteAdapter = new LatestFavoriteAdapter(context, latestFavoriteList);
        recyclerViewLatestFavorite.setAdapter(latestFavoriteAdapter);
        recyclerViewLatestFavorite.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
    }

    private void getUserLatestReview(JSONArray jsonArray) {
        try {
            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject jsonObject = jsonArray.getJSONObject(index);

                Integer id = jsonObject.getInt("id");
                Double rating = jsonObject.getDouble("rating");
                String poster = jsonObject.getJSONObject("film").getString("poster");

                LatestReview latestReview = new LatestReview(id, rating, poster);
                latestReview.setId(id);
                latestReview.setRating(rating);
                latestReview.setPoster(poster);
                latestReviewList.add(latestReview);
            }
        } catch (JSONException error) {
            error.printStackTrace();
        }

        LatestReviewAdapter latestReviewAdapter = new LatestReviewAdapter(context, latestReviewList);
        recyclerViewLatestReview.setAdapter(latestReviewAdapter);
        recyclerViewLatestReview.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
    }
}