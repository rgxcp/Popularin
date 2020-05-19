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

import java.util.List;

import xyz.fairportstudios.popularin.adapters.RecentFavoriteAdapter;
import xyz.fairportstudios.popularin.adapters.RecentReviewAdapter;
import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.models.RecentFavorite;
import xyz.fairportstudios.popularin.models.RecentReview;
import xyz.fairportstudios.popularin.models.AccountDetail;

public class AccountDetailRequest {
    private Context context;
    private String id;
    private List<RecentFavorite> recentFavoriteList;
    private List<RecentReview> recentReviewList;
    private RecyclerView recyclerRecentFavorite;
    private RecyclerView recyclerRecentReview;

    public AccountDetailRequest(
            Context context,
            String id,
            List<RecentFavorite> recentFavoriteList,
            List<RecentReview> recentReviewList,
            RecyclerView recyclerRecentFavorite,
            RecyclerView recyclerRecentReview
    ) {
        this.context = context;
        this.id = id;
        this.recentFavoriteList = recentFavoriteList;
        this.recentReviewList = recentReviewList;
        this.recyclerRecentFavorite = recyclerRecentFavorite;
        this.recyclerRecentReview = recyclerRecentReview;
    }

    public interface APICallback {
        void onSuccess(AccountDetail accountDetail);

        void onEmptyFavorite();

        void onEmptyReview();

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        String requestURL = PopularinAPI.USER + "/" + id;

        JsonObjectRequest accountDetail = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject result = response.getJSONObject("result");
                        JSONObject user = result.getJSONObject("user");
                        JSONObject metadata = result.getJSONObject("metadata");
                        JSONObject activity = result.getJSONObject("activity");

                        AccountDetail accountDetail = new AccountDetail();
                        accountDetail.setTotal_following(metadata.getInt("total_following"));
                        accountDetail.setTotal_follower(metadata.getInt("total_follower"));
                        accountDetail.setTotal_favorite(metadata.getInt("total_favorite"));
                        accountDetail.setTotal_review(metadata.getInt("total_review"));
                        accountDetail.setTotal_watchlist(metadata.getInt("total_watchlist"));
                        accountDetail.setJoined_since(metadata.getString("joined_since"));
                        accountDetail.setFull_name(user.getString("full_name"));
                        accountDetail.setUsername(user.getString("username"));
                        accountDetail.setProfile_picture(user.getString("profile_picture"));
                        callback.onSuccess(accountDetail);

                        int totalFavorite = metadata.getInt("total_favorite");
                        int totalReview = metadata.getInt("total_review");

                        if (totalFavorite > 0) {
                            JSONArray recentFavorites = activity.getJSONArray("recent_favorites");
                            getRecentFavorites(recentFavorites);
                        } else {
                            callback.onEmptyFavorite();
                        }

                        if (totalReview > 0) {
                            JSONArray recentReviews = activity.getJSONArray("recent_reviews");
                            getRecentReviews(recentReviews);
                        } else {
                            callback.onEmptyReview();
                        }
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
        });

        Volley.newRequestQueue(context).add(accountDetail);
    }

    private void getRecentFavorites(JSONArray recentFavorites) {
        try {
            for (int index = 0; index < recentFavorites.length(); index++) {
                JSONObject response = recentFavorites.getJSONObject(index);
                JSONObject film = response.getJSONObject("film");

                RecentFavorite recentFavorite = new RecentFavorite();
                recentFavorite.setTmdb_id(film.getInt("tmdb_id"));
                recentFavorite.setTitle(film.getString("title"));
                recentFavorite.setRelease_date(film.getString("release_date"));
                recentFavorite.setPoster(film.getString("poster"));
                recentFavoriteList.add(recentFavorite);
            }

            RecentFavoriteAdapter recentFavoriteAdapter = new RecentFavoriteAdapter(context, recentFavoriteList);
            recyclerRecentFavorite.setAdapter(recentFavoriteAdapter);
            recyclerRecentFavorite.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            recyclerRecentFavorite.setVisibility(View.VISIBLE);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    private void getRecentReviews(JSONArray recentReviews) {
        try {
            for (int index = 0; index < recentReviews.length(); index++) {
                JSONObject response = recentReviews.getJSONObject(index);
                JSONObject film = response.getJSONObject("film");

                RecentReview latestReview = new RecentReview();
                latestReview.setId(response.getInt("id"));
                latestReview.setTmdb_id(film.getInt("tmdb_id"));
                latestReview.setRating(response.getDouble("rating"));
                latestReview.setTitle(film.getString("title"));
                latestReview.setRelease_date(film.getString("release_date"));
                latestReview.setPoster(film.getString("poster"));
                recentReviewList.add(latestReview);
            }

            RecentReviewAdapter recentReviewAdapter = new RecentReviewAdapter(context, id, recentReviewList);
            recyclerRecentReview.setAdapter(recentReviewAdapter);
            recyclerRecentReview.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            recyclerRecentReview.setVisibility(View.VISIBLE);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
}
