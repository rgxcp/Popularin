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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.fairportstudios.popularin.adapters.FilmAdapter;
import xyz.fairportstudios.popularin.adapters.LatestFavoriteAdapter;
import xyz.fairportstudios.popularin.adapters.LatestReviewAdapter;
import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.models.LatestFavorite;
import xyz.fairportstudios.popularin.models.LatestReview;
import xyz.fairportstudios.popularin.preferences.Auth;

public class UserDetail {
    private String id;
    private Context context;
    private List<LatestFavorite> latestFavoriteList;
    private List<LatestReview> latestReviewList;
    private RecyclerView recyclerViewLatestFavorite;
    private RecyclerView recyclerViewLatestReview;

    public UserDetail(String id, Context context, List<LatestFavorite> latestFavoriteList, List<LatestReview> latestReviewList, RecyclerView recyclerViewLatestFavorite, RecyclerView recyclerViewLatestReview) {
        this.id = id;
        this.context = context;
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

    public void sendRequest(final JSONCallback callback) {
        String requestURL = PopularinAPI.USER + "/" + id;

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
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("auth_uid", new Auth(context).getAuthID());
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    private void getUserLatestFavorite(JSONArray jsonArray) {
        try {
            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject jsonObject = jsonArray.getJSONObject(index);

                LatestFavorite latestFavorite = new LatestFavorite();
                latestFavorite.setTmdb_id(jsonObject.getJSONObject("film").getInt("tmdb_id"));
                latestFavorite.setPoster(jsonObject.getJSONObject("film").getString("poster"));

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

                LatestReview latestReview = new LatestReview();
                latestReview.setId(jsonObject.getInt("id"));
                latestReview.setRating(jsonObject.getDouble("rating"));
                latestReview.setPoster(jsonObject.getJSONObject("film").getString("poster"));

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
