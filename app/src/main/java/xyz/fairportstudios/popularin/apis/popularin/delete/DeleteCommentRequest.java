package xyz.fairportstudios.popularin.apis.popularin.delete;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.fairportstudios.popularin.adapters.CommentAdapter;
import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.models.Comment;
import xyz.fairportstudios.popularin.preferences.Auth;

public class DeleteCommentRequest {
    private Context context;
    private int position;
    private List<Comment> commentList;
    private String commentID;

    public DeleteCommentRequest(Context context, Integer position, List<Comment> commentList, String commentID) {
        this.context = context;
        this.position = position;
        this.commentList = commentList;
        this.commentID = commentID;
    }

    public interface APICallback {
        void onSuccess();

        void onFailed();

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        String requestURL = PopularinAPI.COMMENT + "/" + commentID;

        StringRequest deleteCommentRequest = new StringRequest(Request.Method.DELETE, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");

                    if (status == 404) {
                        CommentAdapter commentAdapter = new CommentAdapter(context, commentList);
                        commentList.remove(position);
                        commentAdapter.notifyItemRemoved(position);
                        callback.onSuccess();
                    } else {
                        callback.onFailed();
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
                headers.put("auth_token", new Auth(context).getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(deleteCommentRequest);
    }
}
