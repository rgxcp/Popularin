package xyz.fairportstudios.popularin.apis.popularin.get;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import xyz.fairportstudios.popularin.statics.PopularinAPI;
import xyz.fairportstudios.popularin.models.Comment;

public class CommentRequest {
    private Context context;
    private Integer reviewID;
    private List<Comment> commentList;

    public CommentRequest(Context context, Integer reviewID, List<Comment> commentList) {
        this.context = context;
        this.reviewID = reviewID;
        this.commentList = commentList;
    }

    public interface APICallback {
        void onSuccess(List<Comment> comments);

        void onEmpty();

        void onError();
    }

    public void sendRequest(Integer page, final APICallback callback) {
        String requestURL = PopularinAPI.REVIEW + "/" + reviewID + "/comments?page=" + page;

        JsonObjectRequest comment = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONArray dataArray = resultObject.getJSONArray("data");

                        for (int index = 0; index < dataArray.length(); index++) {
                            JSONObject commentObject = dataArray.getJSONObject(index);
                            JSONObject userObject = commentObject.getJSONObject("user");

                            Comment comment = new Comment();
                            comment.setId(commentObject.getInt("id"));
                            comment.setUser_id(userObject.getInt("id"));
                            comment.setComment_detail(commentObject.getString("comment_detail"));
                            comment.setTimestamp(commentObject.getString("timestamp"));
                            comment.setUsername(userObject.getString("username"));
                            comment.setProfile_picture(userObject.getString("profile_picture"));
                            commentList.add(comment);
                        }

                        callback.onSuccess(commentList);
                    } else if (status == 606) {
                        callback.onEmpty();
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

        Volley.newRequestQueue(context).add(comment);
    }
}
