package xyz.fairportstudios.popularin.apis.popularin.delete;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
    private int position;
    private Context context;
    private List<Comment> commentList;
    private String id;

    public DeleteCommentRequest(int position, Context context, List<Comment> commentList, String id) {
        this.position = position;
        this.context = context;
        this.commentList = commentList;
        this.id = id;
    }

    public interface APICallback {
        void onSuccess();

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        String requestURL = PopularinAPI.COMMENT + "/" + id;

        JsonObjectRequest deleteCommentRequest = new JsonObjectRequest(Request.Method.DELETE, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 404) {
                        CommentAdapter commentAdapter = new CommentAdapter(context, commentList);
                        commentList.remove(position);
                        commentAdapter.notifyItemRemoved(position);
                        callback.onSuccess();
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
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("auth_token", new Auth(context).getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(deleteCommentRequest);
    }
}
