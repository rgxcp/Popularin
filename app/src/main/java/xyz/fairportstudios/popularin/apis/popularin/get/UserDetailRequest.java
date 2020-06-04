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

import xyz.fairportstudios.popularin.adapters.RecentFavoriteAdapter;
import xyz.fairportstudios.popularin.adapters.RecentReviewAdapter;
import xyz.fairportstudios.popularin.statics.PopularinAPI;
import xyz.fairportstudios.popularin.models.RecentFavorite;
import xyz.fairportstudios.popularin.models.RecentReview;
import xyz.fairportstudios.popularin.models.UserDetail;
import xyz.fairportstudios.popularin.preferences.Auth;

public class UserDetailRequest {
    private Context context;
    private String id;

    public UserDetailRequest(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    public interface APICallback {
        void onSuccess(UserDetail userDetail);

        void onHasFavorite(JSONArray recentFavorites);

        void onHasReview(JSONArray recentReviews);

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        String requestURL = PopularinAPI.USER + "/" + id;

        JsonObjectRequest userDetail = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONObject userObject = resultObject.getJSONObject("user");
                        JSONObject metadataObject = resultObject.getJSONObject("metadata");
                        JSONObject activityObject = response.getJSONObject("result").getJSONObject("activity");
                        int totalFavorite = metadataObject.getInt("total_favorite");
                        int totalReview = metadataObject.getInt("total_review");

                        UserDetail userDetail = new UserDetail();
                        userDetail.setIs_following(metadataObject.getBoolean("is_following"));
                        userDetail.setIs_follower(metadataObject.getBoolean("is_follower"));
                        userDetail.setTotal_following(metadataObject.getInt("total_following"));
                        userDetail.setTotal_follower(metadataObject.getInt("total_follower"));
                        userDetail.setTotal_favorite(totalFavorite);
                        userDetail.setTotal_review(totalReview);
                        userDetail.setTotal_watchlist(metadataObject.getInt("total_watchlist"));
                        userDetail.setFull_name(userObject.getString("full_name"));
                        userDetail.setUsername(userObject.getString("username"));
                        userDetail.setProfile_picture(userObject.getString("profile_picture"));
                        callback.onSuccess(userDetail);

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
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Auth-ID", new Auth(context).getAuthID());
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(userDetail);
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
