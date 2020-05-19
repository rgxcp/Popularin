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
import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.models.RecentFavorite;
import xyz.fairportstudios.popularin.models.RecentReview;
import xyz.fairportstudios.popularin.models.UserDetail;
import xyz.fairportstudios.popularin.preferences.Auth;

public class UserDetailRequest {
    private Context context;
    private List<RecentFavorite> recentFavoriteList;
    private List<RecentReview> recentReviewList;
    private RecyclerView recyclerViewLatestFavorite;
    private RecyclerView recyclerViewLatestReview;
    private String id;

    public UserDetailRequest(Context context, List<RecentFavorite> recentFavoriteList, List<RecentReview> recentReviewList, RecyclerView recyclerViewLatestFavorite, RecyclerView recyclerViewLatestReview, String id) {
        this.context = context;
        this.recentFavoriteList = recentFavoriteList;
        this.recentReviewList = recentReviewList;
        this.recyclerViewLatestFavorite = recyclerViewLatestFavorite;
        this.recyclerViewLatestReview = recyclerViewLatestReview;
        this.id = id;
    }

    public interface APICallback {
        void onSuccess(UserDetail userDetail);

        void onEmptyFavorite();

        void onEmptyReview();

        void onDeleted();

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        String requestURL = PopularinAPI.USER + "/" + id;

        JsonObjectRequest userDetailRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject jsonObjectResult = response.getJSONObject("result");
                        JSONObject jsonObjectUser = jsonObjectResult.getJSONObject("user");
                        JSONObject jsonObjectMetadata = jsonObjectResult.getJSONObject("metadata");
                        JSONObject jsonObjectActivity = response.getJSONObject("result").getJSONObject("activity");

                        UserDetail userDetail = new UserDetail();
                        userDetail.setFollowingStatus(jsonObjectMetadata.getBoolean("is_following"));
                        userDetail.setFollowerStatus(jsonObjectMetadata.getBoolean("is_follower"));
                        userDetail.setFullName(jsonObjectUser.getString("full_name"));
                        userDetail.setProfilePicture(jsonObjectUser.getString("profile_picture"));
                        userDetail.setRate05(String.valueOf(jsonObjectMetadata.getInt("rate_0.5")));
                        userDetail.setRate10(String.valueOf(jsonObjectMetadata.getInt("rate_1.0")));
                        userDetail.setRate15(String.valueOf(jsonObjectMetadata.getInt("rate_1.5")));
                        userDetail.setRate20(String.valueOf(jsonObjectMetadata.getInt("rate_2.0")));
                        userDetail.setRate25(String.valueOf(jsonObjectMetadata.getInt("rate_2.5")));
                        userDetail.setRate30(String.valueOf(jsonObjectMetadata.getInt("rate_3.0")));
                        userDetail.setRate35(String.valueOf(jsonObjectMetadata.getInt("rate_3.5")));
                        userDetail.setRate40(String.valueOf(jsonObjectMetadata.getInt("rate_4.0")));
                        userDetail.setRate45(String.valueOf(jsonObjectMetadata.getInt("rate_4.5")));
                        userDetail.setRate50(String.valueOf(jsonObjectMetadata.getInt("rate_5.0")));
                        userDetail.setTotalFavorite(String.valueOf(jsonObjectMetadata.getInt("favorites")));
                        userDetail.setTotalFollower(String.valueOf(jsonObjectMetadata.getInt("followers")));
                        userDetail.setTotalFollowing(String.valueOf(jsonObjectMetadata.getInt("followings")));
                        userDetail.setTotalReview(String.valueOf(jsonObjectMetadata.getInt("reviews")));
                        userDetail.setTotalWatchlist(String.valueOf(jsonObjectMetadata.getInt("watchlists")));
                        userDetail.setUsername(String.format("@%s", jsonObjectUser.getString("username")));
                        callback.onSuccess(userDetail);

                        int favorites = jsonObjectMetadata.getInt("favorites");
                        int reviews = jsonObjectMetadata.getInt("reviews");

                        if (favorites == 0) {
                            callback.onEmptyFavorite();
                        } else {
                            JSONArray jsonArrayFavorites = jsonObjectActivity.getJSONArray("favorites");
                            getUserLatestFavorite(jsonArrayFavorites);
                        }

                        if (reviews == 0) {
                            callback.onEmptyReview();
                        } else {
                            JSONArray jsonArrayReviews = jsonObjectActivity.getJSONArray("reviews");
                            getUserLatestReview(jsonArrayReviews);
                        }
                    } else {
                        callback.onDeleted();
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
                headers.put("auth_uid", new Auth(context).getAuthID());
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(userDetailRequest);
    }

    private void getUserLatestFavorite(JSONArray jsonArrayFavorites) {
        try {
            for (int index = 0; index < jsonArrayFavorites.length(); index++) {
                JSONObject jsonObject = jsonArrayFavorites.getJSONObject(index);
                JSONObject jsonObjectFilm = jsonObject.getJSONObject("film");

                RecentFavorite recentFavorite = new RecentFavorite();
                recentFavorite.setTmdb_id(jsonObjectFilm.getInt("tmdb_id"));
                recentFavorite.setPoster(jsonObjectFilm.getString("poster"));
                recentFavorite.setRelease_date(jsonObjectFilm.getString("release_date"));
                recentFavorite.setTitle(jsonObjectFilm.getString("title"));

                recentFavoriteList.add(recentFavorite);
            }

            RecentFavoriteAdapter recentFavoriteAdapter = new RecentFavoriteAdapter(context, recentFavoriteList);
            recyclerViewLatestFavorite.setAdapter(recentFavoriteAdapter);
            recyclerViewLatestFavorite.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            recyclerViewLatestFavorite.setVisibility(View.VISIBLE);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    private void getUserLatestReview(JSONArray jsonArrayReviews) {
        try {
            for (int index = 0; index < jsonArrayReviews.length(); index++) {
                JSONObject jsonObject = jsonArrayReviews.getJSONObject(index);
                JSONObject jsonObjectFilm = jsonObject.getJSONObject("film");

                RecentReview recentReview = new RecentReview();
                recentReview.setId(jsonObject.getInt("id"));
                recentReview.setTmdb_id(jsonObjectFilm.getInt("tmdb_id"));
                recentReview.setRating(jsonObject.getDouble("rating"));
                recentReview.setPoster(jsonObjectFilm.getString("poster"));
                recentReview.setRelease_date(jsonObjectFilm.getString("release_date"));
                recentReview.setTitle(jsonObjectFilm.getString("title"));

                recentReviewList.add(recentReview);
            }

            RecentReviewAdapter recentReviewAdapter = new RecentReviewAdapter(context, id, recentReviewList);
            recyclerViewLatestReview.setAdapter(recentReviewAdapter);
            recyclerViewLatestReview.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            recyclerViewLatestReview.setVisibility(View.VISIBLE);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
}
