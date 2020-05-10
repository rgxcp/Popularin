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

import xyz.fairportstudios.popularin.adapters.LatestFavoriteAdapter;
import xyz.fairportstudios.popularin.adapters.LatestReviewAdapter;
import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.models.LatestFavorite;
import xyz.fairportstudios.popularin.models.LatestReview;
import xyz.fairportstudios.popularin.models.ProfileDetail;

public class ProfileDetailRequest {
    private Context context;
    private List<LatestFavorite> latestFavoriteList;
    private List<LatestReview> latestReviewList;
    private RecyclerView recyclerViewLatestFavorite;
    private RecyclerView recyclerViewLatestReview;
    private String id;

    public ProfileDetailRequest(Context context, List<LatestFavorite> latestFavoriteList, List<LatestReview> latestReviewList, RecyclerView recyclerViewLatestFavorite, RecyclerView recyclerViewLatestReview, String id) {
        this.context = context;
        this.latestFavoriteList = latestFavoriteList;
        this.latestReviewList = latestReviewList;
        this.recyclerViewLatestFavorite = recyclerViewLatestFavorite;
        this.recyclerViewLatestReview = recyclerViewLatestReview;
        this.id = id;
    }

    public interface APICallback {
        void onSuccess(ProfileDetail profileDetail);

        void onEmptyFavorite();

        void onEmptyReview();

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        String requestURL = PopularinAPI.USER + "/" + id;

        JsonObjectRequest profileDetailRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObjectResult = response.getJSONObject("result");
                    JSONObject jsonObjectUser = jsonObjectResult.getJSONObject("user");
                    JSONObject jsonObjectMetadata = jsonObjectResult.getJSONObject("metadata");
                    JSONObject jsonObjectActivity = response.getJSONObject("result").getJSONObject("activity");

                    ProfileDetail profileDetail = new ProfileDetail();
                    profileDetail.setFullName(jsonObjectUser.getString("full_name"));
                    profileDetail.setProfilePicture(jsonObjectUser.getString("profile_picture"));
                    profileDetail.setRate05(String.valueOf(jsonObjectMetadata.getInt("rate_0.5")));
                    profileDetail.setRate10(String.valueOf(jsonObjectMetadata.getInt("rate_1.0")));
                    profileDetail.setRate15(String.valueOf(jsonObjectMetadata.getInt("rate_1.5")));
                    profileDetail.setRate20(String.valueOf(jsonObjectMetadata.getInt("rate_2.0")));
                    profileDetail.setRate25(String.valueOf(jsonObjectMetadata.getInt("rate_2.5")));
                    profileDetail.setRate30(String.valueOf(jsonObjectMetadata.getInt("rate_3.0")));
                    profileDetail.setRate35(String.valueOf(jsonObjectMetadata.getInt("rate_3.5")));
                    profileDetail.setRate40(String.valueOf(jsonObjectMetadata.getInt("rate_4.0")));
                    profileDetail.setRate45(String.valueOf(jsonObjectMetadata.getInt("rate_4.5")));
                    profileDetail.setRate50(String.valueOf(jsonObjectMetadata.getInt("rate_5.0")));
                    profileDetail.setTotalFavorite(String.valueOf(jsonObjectMetadata.getInt("favorites")));
                    profileDetail.setTotalFollower(String.valueOf(jsonObjectMetadata.getInt("followers")));
                    profileDetail.setTotalFollowing(String.valueOf(jsonObjectMetadata.getInt("followings")));
                    profileDetail.setTotalReview(String.valueOf(jsonObjectMetadata.getInt("reviews")));
                    profileDetail.setTotalWatchlist(String.valueOf(jsonObjectMetadata.getInt("watchlists")));
                    profileDetail.setUsername(String.format("@%s", jsonObjectUser.getString("username")));
                    callback.onSuccess(profileDetail);

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

        Volley.newRequestQueue(context).add(profileDetailRequest);
    }

    private void getUserLatestFavorite(JSONArray jsonArrayFavorites) {
        try {
            for (int index = 0; index < jsonArrayFavorites.length(); index++) {
                JSONObject jsonObject = jsonArrayFavorites.getJSONObject(index);

                LatestFavorite latestFavorite = new LatestFavorite();
                latestFavorite.setTmdb_id(jsonObject.getJSONObject("film").getInt("tmdb_id"));
                latestFavorite.setPoster(jsonObject.getJSONObject("film").getString("poster"));

                latestFavoriteList.add(latestFavorite);
            }

            LatestFavoriteAdapter latestFavoriteAdapter = new LatestFavoriteAdapter(context, latestFavoriteList);
            recyclerViewLatestFavorite.setAdapter(latestFavoriteAdapter);
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

                LatestReview latestReview = new LatestReview();
                latestReview.setId(jsonObject.getInt("id"));
                latestReview.setRating(jsonObject.getDouble("rating"));
                latestReview.setPoster(jsonObject.getJSONObject("film").getString("poster"));

                latestReviewList.add(latestReview);
            }

            LatestReviewAdapter latestReviewAdapter = new LatestReviewAdapter(context, latestReviewList);
            recyclerViewLatestReview.setAdapter(latestReviewAdapter);
            recyclerViewLatestReview.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            recyclerViewLatestReview.setVisibility(View.VISIBLE);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
}
