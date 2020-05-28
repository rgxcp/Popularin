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

    public AccountDetailRequest(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    public interface APICallback {
        void onSuccess(AccountDetail accountDetail);

        void onHasFavorite(JSONArray recentFavorites);

        void onHasReview(JSONArray recentReviews);

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
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONObject userObject = resultObject.getJSONObject("user");
                        JSONObject metadataObject = resultObject.getJSONObject("metadata");
                        JSONObject activityObject = resultObject.getJSONObject("activity");
                        int totalFavorite = metadataObject.getInt("total_favorite");
                        int totalReview = metadataObject.getInt("total_review");

                        AccountDetail accountDetail = new AccountDetail();
                        accountDetail.setTotal_following(metadataObject.getInt("total_following"));
                        accountDetail.setTotal_follower(metadataObject.getInt("total_follower"));
                        accountDetail.setTotal_favorite(totalFavorite);
                        accountDetail.setTotal_review(totalReview);
                        accountDetail.setTotal_watchlist(metadataObject.getInt("total_watchlist"));
                        accountDetail.setFull_name(userObject.getString("full_name"));
                        accountDetail.setUsername(userObject.getString("username"));
                        accountDetail.setProfile_picture(userObject.getString("profile_picture"));
                        callback.onSuccess(accountDetail);

                        if (totalFavorite > 0) {
                            JSONArray recentFavoriteArray = activityObject.getJSONArray("recent_favorites");
                            callback.onHasFavorite(recentFavoriteArray);
                        }
                        if (totalReview > 0) {
                            JSONArray recentReviewArray = activityObject.getJSONArray("recent_reviews");
                            callback.onHasReview(recentReviewArray);
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

    public void getRecentFavorites(JSONArray recentFavorites, List<RecentFavorite> recentFavoriteList, RecyclerView recyclerView) {
        try {
            for (int index = 0; index < recentFavorites.length(); index++) {
                JSONObject indexObject = recentFavorites.getJSONObject(index);
                JSONObject filmObject = indexObject.getJSONObject("film");

                RecentFavorite recentFavorite = new RecentFavorite();
                recentFavorite.setTmdb_id(filmObject.getInt("tmdb_id"));
                recentFavorite.setTitle(filmObject.getString("title"));
                recentFavorite.setRelease_date(filmObject.getString("release_date"));
                recentFavorite.setPoster(filmObject.getString("poster"));
                recentFavoriteList.add(recentFavorite);
            }

            RecentFavoriteAdapter recentFavoriteAdapter = new RecentFavoriteAdapter(context, recentFavoriteList);
            recyclerView.setAdapter(recentFavoriteAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            recyclerView.setHasFixedSize(true);
            recyclerView.setVisibility(View.VISIBLE);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    public void getRecentReviews(JSONArray recentReviews, List<RecentReview> recentReviewList, RecyclerView recyclerView) {
        try {
            for (int index = 0; index < recentReviews.length(); index++) {
                JSONObject indexObject = recentReviews.getJSONObject(index);
                JSONObject filmObject = indexObject.getJSONObject("film");

                RecentReview latestReview = new RecentReview();
                latestReview.setId(indexObject.getInt("id"));
                latestReview.setTmdb_id(filmObject.getInt("tmdb_id"));
                latestReview.setRating(indexObject.getDouble("rating"));
                latestReview.setTitle(filmObject.getString("title"));
                latestReview.setRelease_date(filmObject.getString("release_date"));
                latestReview.setPoster(filmObject.getString("poster"));
                recentReviewList.add(latestReview);
            }

            RecentReviewAdapter recentReviewAdapter = new RecentReviewAdapter(context, recentReviewList, id);
            recyclerView.setAdapter(recentReviewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            recyclerView.setHasFixedSize(true);
            recyclerView.setVisibility(View.VISIBLE);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
}
