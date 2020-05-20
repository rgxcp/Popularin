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

import xyz.fairportstudios.popularin.adapters.CommentAdapter;
import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.models.Comment;

public class CommentRequest {
    private Context context;
    private List<Comment> commentList;
    private RecyclerView recyclerView;

    public CommentRequest(Context context, List<Comment> commentList, RecyclerView recyclerView) {
        this.context = context;
        this.commentList = commentList;
        this.recyclerView = recyclerView;
    }

    public interface APICallback {
        void onSuccess();

        void onEmpty();

        void onError();
    }

    public String getRequestURL(String id, Integer page) {
        return PopularinAPI.REVIEW
                + "/"
                + id
                + "/comments?page="
                + page;
    }

    public void sendRequest(String requestURL, final APICallback callback) {
        JsonObjectRequest commenRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONArray jsonArrayData = response.getJSONObject("result").getJSONArray("data");

                        for (int index = 0; index < jsonArrayData.length(); index++) {
                            JSONObject objectIndex = jsonArrayData.getJSONObject(index);
                            JSONObject userObject = objectIndex.getJSONObject("user");

                            Comment comment = new Comment();
                            comment.setId(objectIndex.getInt("id"));
                            comment.setUser_id(userObject.getInt("id"));
                            comment.setComment_detail(objectIndex.getString("comment_detail"));
                            comment.setComment_text(objectIndex.getString("comment_detail"));
                            comment.setTimestamp(objectIndex.getString("timestamp"));
                            comment.setFirst_name(userObject.getString("first_name"));
                            comment.setProfile_picture(userObject.getString("profile_picture"));
                            commentList.add(comment);
                        }

                        CommentAdapter commentAdapter = new CommentAdapter(context, commentList);
                        recyclerView.setAdapter(commentAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setVisibility(View.VISIBLE);
                        callback.onSuccess();
                    } else {
                        callback.onEmpty();
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

        Volley.newRequestQueue(context).add(commenRequest);
    }
}
