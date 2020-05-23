package xyz.fairportstudios.popularin.apis.popularin.post;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.models.Comment;
import xyz.fairportstudios.popularin.preferences.Auth;

public class AddCommentRequest {
    private Context context;
    private String reviewID;
    private String comment;

    public AddCommentRequest(Context context, String reviewID, String comment) {
        this.context = context;
        this.reviewID = reviewID;
        this.comment = comment;
    }

    public interface APICallback {
        void onSuccess(Comment comment);

        void onFailed(String message);

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        final Auth auth = new Auth(context);

        String requestURL = PopularinAPI.COMMENT;

        StringRequest addComment = new StringRequest(Request.Method.POST, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObject = new JSONObject(response);
                    int status = responseObject.getInt("status");

                    if (status == 202) {
                        JSONObject resultObject = responseObject.getJSONObject("result");
                        JSONObject commentObject = resultObject.getJSONObject("comment");
                        JSONObject userObject = resultObject.getJSONObject("user");

                        Comment comment = new Comment();
                        comment.setId(commentObject.getInt("id"));
                        comment.setUser_id(userObject.getInt("id"));
                        comment.setComment_detail(commentObject.getString("comment_detail"));
                        comment.setTimestamp(commentObject.getString("timestamp"));
                        comment.setUsername(userObject.getString("username"));
                        comment.setProfile_picture(userObject.getString("profile_picture"));
                        callback.onSuccess(comment);
                    } else if (status == 626) {
                        JSONArray resultArray = responseObject.getJSONArray("result");
                        String message = resultArray.getString(0);
                        callback.onFailed(message);
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
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("review_id", reviewID);
                params.put("comment_detail", comment);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Auth-ID", auth.getAuthID());
                headers.put("Auth-Token", auth.getAuthToken());
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(addComment);
    }
}
