package xyz.fairportstudios.popularin.apis.popularin.get;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.RecentFavoriteAdapter;
import xyz.fairportstudios.popularin.adapters.RecentReviewAdapter;
import xyz.fairportstudios.popularin.statics.PopularinAPI;
import xyz.fairportstudios.popularin.models.RecentFavorite;
import xyz.fairportstudios.popularin.models.RecentReview;
import xyz.fairportstudios.popularin.models.UserDetail;
import xyz.fairportstudios.popularin.preferences.Auth;

public class UserDetailRequest {
    private Context context;
    private Integer id;

    public UserDetailRequest(Context context, Integer id) {
        this.context = context;
        this.id = id;
    }

    public interface Callback {
        void onSuccess(UserDetail userDetail);

        void onHasFavorite(JSONArray recentFavoriteArray);

        void onHasReview(JSONArray recentReviewArray);

        void onError(String message);
    }

    public void sendRequest(final Callback callback) {
        String requestURL = PopularinAPI.USER + id;

        JsonObjectRequest userDetail = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
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

                        UserDetail userDetail = new UserDetail();
                        userDetail.setIs_follower(metadataObject.getBoolean("is_follower"));
                        userDetail.setIs_following(metadataObject.getBoolean("is_following"));
                        userDetail.setTotal_review(totalReview);
                        userDetail.setTotal_favorite(totalFavorite);
                        userDetail.setTotal_watchlist(metadataObject.getInt("total_watchlist"));
                        userDetail.setTotal_follower(metadataObject.getInt("total_follower"));
                        userDetail.setTotal_following(metadataObject.getInt("total_following"));
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
                        callback.onError(context.getString(R.string.general_error));
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                    callback.onError(context.getString(R.string.general_error));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof NetworkError || error instanceof TimeoutError) {
                    callback.onError(context.getString(R.string.network_error));
                } else if (error instanceof ServerError) {
                    callback.onError(context.getString(R.string.server_error));
                } else {
                    callback.onError(context.getString(R.string.general_error));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("API-Key", PopularinAPI.API_KEY);
                headers.put("Auth-Token", new Auth(context).getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(userDetail);
    }

    public void getRecentFavorites(JSONArray recentFavoriteArray, RecyclerView recyclerView) {
        try {
            List<RecentFavorite> recentFavoriteList = new ArrayList<>();

            for (int index = 0; index < recentFavoriteArray.length(); index++) {
                JSONObject indexObject = recentFavoriteArray.getJSONObject(index);
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

    public void getRecentReviews(JSONArray recentReviewArray, RecyclerView recyclerView) {
        try {
            List<RecentReview> recentReviewList = new ArrayList<>();

            for (int index = 0; index < recentReviewArray.length(); index++) {
                JSONObject indexObject = recentReviewArray.getJSONObject(index);
                JSONObject filmObject = indexObject.getJSONObject("film");

                RecentReview recentReview = new RecentReview();
                recentReview.setId(indexObject.getInt("id"));
                recentReview.setTmdb_id(filmObject.getInt("tmdb_id"));
                recentReview.setRating(indexObject.getDouble("rating"));
                recentReview.setTitle(filmObject.getString("title"));
                recentReview.setRelease_date(filmObject.getString("release_date"));
                recentReview.setPoster(filmObject.getString("poster"));
                recentReviewList.add(recentReview);
            }

            RecentReviewAdapter recentReviewAdapter = new RecentReviewAdapter(context, recentReviewList, true);
            recyclerView.setAdapter(recentReviewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            recyclerView.setHasFixedSize(true);
            recyclerView.setVisibility(View.VISIBLE);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
}
