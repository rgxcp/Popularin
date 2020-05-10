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

import xyz.fairportstudios.popularin.adapters.UserReviewAdapter;
import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.models.UserReview;

public class UserReviewRequest {
    private Context context;
    private List<UserReview> userReviewList;
    private RecyclerView recyclerView;

    public UserReviewRequest(Context context, List<UserReview> userReviewList, RecyclerView recyclerView) {
        this.context = context;
        this.userReviewList = userReviewList;
        this.recyclerView = recyclerView;
    }

    public interface APICallback {
        void onSuccess();

        void onEmpty();

        void onError();
    }

    public String getRequestURL(String id, Integer page) {
        return PopularinAPI.USER
                + "/"
                + id
                + "/reviews?page="
                + page;
    }

    public void sendRequest(String requestURL, final APICallback callback) {
        JsonObjectRequest userReviewRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONArray jsonArrayData = response.getJSONObject("result").getJSONArray("data");

                        for (int index = 0; index < jsonArrayData.length(); index++) {
                            JSONObject jsonObject = jsonArrayData.getJSONObject(index);
                            JSONObject jsonObjectFilm = jsonObject.getJSONObject("film");

                            UserReview userReview = new UserReview();
                            userReview.setId(jsonObject.getInt("id"));
                            userReview.setTmdb_id(jsonObjectFilm.getInt("tmdb_id"));
                            userReview.setRating(jsonObject.getDouble("rating"));
                            userReview.setPoster(jsonObjectFilm.getString("poster"));
                            userReview.setRelease_date(jsonObjectFilm.getString("release_date"));
                            userReview.setReview_date(jsonObject.getString("review_date"));
                            userReview.setReview_text(jsonObject.getString("review_text"));
                            userReview.setTitle(jsonObjectFilm.getString("title"));

                            userReviewList.add(userReview);
                        }

                        UserReviewAdapter userReviewAdapter = new UserReviewAdapter(context, userReviewList);
                        recyclerView.setAdapter(userReviewAdapter);
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

        Volley.newRequestQueue(context).add(userReviewRequest);
    }
}
