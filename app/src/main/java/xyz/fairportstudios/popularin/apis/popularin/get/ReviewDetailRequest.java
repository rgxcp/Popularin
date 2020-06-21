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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.ReviewDetail;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.secrets.APIKey;
import xyz.fairportstudios.popularin.statics.PopularinAPI;

public class ReviewDetailRequest {
    private Context mContext;
    private int mReviewID;

    public ReviewDetailRequest(Context context, int reviewID) {
        mContext = context;
        mReviewID = reviewID;
    }

    public interface Callback {
        void onSuccess(ReviewDetail reviewDetail);

        void onError(String message);
    }

    public void sendRequest(final Callback callback) {
        String requestURL = PopularinAPI.REVIEW + mReviewID;

        JsonObjectRequest reviewDetail = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONObject filmObject = resultObject.getJSONObject("film");
                        JSONObject userObject = resultObject.getJSONObject("user");

                        ReviewDetail reviewDetail = new ReviewDetail(
                                filmObject.getInt("tmdb_id"),
                                userObject.getInt("id"),
                                resultObject.getInt("total_like"),
                                resultObject.getBoolean("is_liked"),
                                resultObject.getDouble("rating"),
                                resultObject.getString("review_detail"),
                                resultObject.getString("review_date"),
                                resultObject.getString("watch_date"),
                                filmObject.getString("title"),
                                filmObject.getString("release_date"),
                                filmObject.getString("poster"),
                                userObject.getString("username"),
                                userObject.getString("profile_picture")
                        );

                        callback.onSuccess(reviewDetail);
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

        Volley.newRequestQueue(mContext).add(reviewDetail);
    }
}
