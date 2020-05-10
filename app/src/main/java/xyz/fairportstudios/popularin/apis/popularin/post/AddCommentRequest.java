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
import java.util.List;
import java.util.Map;

import xyz.fairportstudios.popularin.adapters.CommentAdapter;
import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.models.Comment;
import xyz.fairportstudios.popularin.preferences.Auth;

public class AddCommentRequest {
    private Context context;
    private List<Comment> commentList;
    private String id;
    private String comment;

    public AddCommentRequest(Context context, List<Comment> commentList, String id, String comment) {
        this.context = context;
        this.commentList = commentList;
        this.id = id;
        this.comment = comment;
    }

    public interface APICallback {
        void onSuccess();

        void onFailed(String message);

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        final Auth auth = new Auth(context);

        String requestURL = PopularinAPI.COMMENT;

        StringRequest addCommentRequest = new StringRequest(Request.Method.POST, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");

                    if (status == 202) {
                        JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
                        JSONObject jsonObjectComment = jsonObjectResult.getJSONObject("comment");
                        JSONObject jsonObjectUser = jsonObjectResult.getJSONObject("user");

                        Comment comment = new Comment();
                        comment.setId(jsonObjectComment.getInt("id"));
                        comment.setUser_id(jsonObjectUser.getInt("id"));
                        comment.setComment_date(jsonObjectComment.getString("comment_date"));
                        comment.setComment_text(jsonObjectComment.getString("comment_text"));
                        comment.setFirst_name(jsonObjectUser.getString("first_name"));
                        comment.setProfile_picture(jsonObjectUser.getString("profile_picture"));

                        commentList.add(comment);
                        CommentAdapter commentAdapter = new CommentAdapter(context, commentList);
                        commentAdapter.notifyItemInserted(commentList.size());
                        callback.onSuccess();
                    } else if (status == 626) {
                        JSONArray jsonArrayResult = jsonObject.getJSONArray("result");
                        String message = jsonArrayResult.getString(0);
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
                params.put("review_id", id);
                params.put("comment_text", comment);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("auth_uid", auth.getAuthID());
                headers.put("auth_token", auth.getAuthToken());
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(addCommentRequest);
    }
}
