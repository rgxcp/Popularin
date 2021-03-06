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
import xyz.fairportstudios.popularin.models.UserReview;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.secrets.APIKey;
import xyz.fairportstudios.popularin.statics.PopularinAPI;

public class UserReviewRequest {
    private Context mContext;
    private int mUserID;

    public UserReviewRequest(Context context, int userID) {
        mContext = context;
        mUserID = userID;
    }

    public interface Callback {
        void onSuccess(int totalPage, List<UserReview> userReviewList);

        void onNotFound();

        void onError(String message);
    }

    public void sendRequest(int page, final Callback callback) {
        String requestURL = PopularinAPI.USER + mUserID + "/reviews?page=" + page;

        JsonObjectRequest userReview = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        List<UserReview> userReviewList = new ArrayList<>();
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONArray dataArray = resultObject.getJSONArray("data");
                        int totalPage = resultObject.getInt("last_page");

                        for (int index = 0; index < dataArray.length(); index++) {
                            JSONObject indexObject = dataArray.getJSONObject(index);
                            JSONObject filmObject = indexObject.getJSONObject("film");

                            UserReview userReview = new UserReview(
                                    indexObject.getInt("id"),
                                    filmObject.getInt("tmdb_id"),
                                    indexObject.getInt("total_like"),
                                    indexObject.getInt("total_comment"),
                                    indexObject.getBoolean("is_liked"),
                                    indexObject.getDouble("rating"),
                                    indexObject.getString("review_detail"),
                                    indexObject.getString("timestamp"),
                                    filmObject.getString("title"),
                                    filmObject.getString("release_date"),
                                    filmObject.getString("poster")
                            );

                            userReviewList.add(userReview);
                        }

                        callback.onSuccess(totalPage, userReviewList);
                    } else if (status == 606) {
                        callback.onNotFound();
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
                headers.put("Auth-Token", new Auth(mContext).getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(mContext).add(userReview);
    }
}
