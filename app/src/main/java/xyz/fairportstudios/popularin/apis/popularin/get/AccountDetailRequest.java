package xyz.fairportstudios.popularin.apis.popularin.get;

import android.content.Context;

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
import xyz.fairportstudios.popularin.models.AccountDetail;
import xyz.fairportstudios.popularin.models.RecentFavorite;
import xyz.fairportstudios.popularin.models.RecentReview;
import xyz.fairportstudios.popularin.secrets.APIKey;
import xyz.fairportstudios.popularin.statics.PopularinAPI;

public class AccountDetailRequest {
    private Context mContext;
    private int mUserID;

    public AccountDetailRequest(Context context, int userID) {
        mContext = context;
        mUserID = userID;
    }

    public interface Callback {
        void onSuccess(AccountDetail accountDetail);

        void onHasRecentFavorite(List<RecentFavorite> recentFavoriteList);

        void onHasRecentReview(List<RecentReview> recentReviewList);

        void onError(String message);
    }

    public void sendRequest(final Callback callback) {
        String requestURL = PopularinAPI.USER + mUserID;

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
                        JSONArray recentFavoriteArray = activityObject.getJSONArray("recent_favorites");
                        JSONArray recentReviewArray = activityObject.getJSONArray("recent_reviews");

                        // Detail
                        AccountDetail accountDetail = new AccountDetail(
                                metadataObject.getInt("total_review"),
                                metadataObject.getInt("total_favorite"),
                                metadataObject.getInt("total_watchlist"),
                                metadataObject.getInt("total_follower"),
                                metadataObject.getInt("total_following"),
                                userObject.getString("full_name"),
                                userObject.getString("username"),
                                userObject.getString("profile_picture")
                        );

                        callback.onSuccess(accountDetail);

                        // Favorite
                        if (recentFavoriteArray.length() != 0) {
                            List<RecentFavorite> recentFavoriteList = new ArrayList<>();

                            for (int index = 0; index < recentFavoriteArray.length(); index++) {
                                JSONObject indexObject = recentFavoriteArray.getJSONObject(index);
                                JSONObject filmObject = indexObject.getJSONObject("film");

                                RecentFavorite recentFavorite = new RecentFavorite(
                                        filmObject.getInt("tmdb_id"),
                                        filmObject.getString("title"),
                                        filmObject.getString("release_date"),
                                        filmObject.getString("poster")
                                );

                                recentFavoriteList.add(recentFavorite);
                            }

                            callback.onHasRecentFavorite(recentFavoriteList);
                        }

                        // Review
                        if (recentReviewArray.length() != 0) {
                            List<RecentReview> recentReviewList = new ArrayList<>();

                            for (int index = 0; index < recentReviewArray.length(); index++) {
                                JSONObject indexObject = recentReviewArray.getJSONObject(index);
                                JSONObject filmObject = indexObject.getJSONObject("film");

                                RecentReview recentReview = new RecentReview(
                                        indexObject.getInt("id"),
                                        filmObject.getInt("tmdb_id"),
                                        indexObject.getDouble("rating"),
                                        filmObject.getString("title"),
                                        filmObject.getString("release_date"),
                                        filmObject.getString("poster")
                                );

                                recentReviewList.add(recentReview);
                            }

                            callback.onHasRecentReview(recentReviewList);
                        }
                    } else {
                        callback.onError(mContext.getString(R.string.general_error));
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                    callback.onError(mContext.getString(R.string.general_error));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof NetworkError || error instanceof TimeoutError) {
                    callback.onError(mContext.getString(R.string.network_error));
                } else if (error instanceof ServerError) {
                    callback.onError(mContext.getString(R.string.server_error));
                } else {
                    callback.onError(mContext.getString(R.string.general_error));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("API-Key", APIKey.POPULARIN_API_KEY);
                return headers;
            }
        };

        Volley.newRequestQueue(mContext).add(accountDetail);
    }
}
