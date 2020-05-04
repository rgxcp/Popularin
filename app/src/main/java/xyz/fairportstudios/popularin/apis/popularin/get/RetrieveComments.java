package xyz.fairportstudios.popularin.apis.popularin.get;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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

public class RetrieveComments {
    private String id;
    private Context context;
    private List<Comment> commentList;
    private RecyclerView recyclerView;

    public RetrieveComments(String id, Context context, List<Comment> commentList, RecyclerView recyclerView) {
        this.id = id;
        this.context = context;
        this.commentList = commentList;
        this.recyclerView = recyclerView;
    }

    public interface JSONCallback {
        void onSuccess(Integer status);
    }

    public void sendRequest(final JSONCallback callback) {
        String requestURL = PopularinAPI.REVIEW + "/" + id + "/comments";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");
                    callback.onSuccess(status);

                    if (status == 101) {
                        JSONArray jsonArrayData = response.getJSONObject("result").getJSONArray("data");

                        for (int index = 0; index < jsonArrayData.length(); index++) {
                            JSONObject jsonObject = jsonArrayData.getJSONObject(index);

                            Integer id = jsonObject.getInt("id");
                            Integer user_id = jsonObject.getJSONObject("user").getInt("id");
                            String comment_date = jsonObject.getString("comment_date");
                            String comment_text = jsonObject.getString("comment_text");
                            String first_name = jsonObject.getJSONObject("user").getString("first_name");
                            String profile_picture = jsonObject.getJSONObject("user").getString("profile_picture");

                            Comment comment = new Comment(id, user_id, comment_date, comment_text, first_name, profile_picture);
                            comment.setId(id);
                            comment.setUser_id(user_id);
                            comment.setComment_date(comment_date);
                            comment.setComment_text(comment_text);
                            comment.setFirst_name(first_name);
                            comment.setProfile_picture(profile_picture);

                            commentList.add(comment);
                        }

                        CommentAdapter commentAdapter = new CommentAdapter(context, commentList);
                        recyclerView.setAdapter(commentAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    }
                } catch (JSONException error) {
                    error.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }
}
