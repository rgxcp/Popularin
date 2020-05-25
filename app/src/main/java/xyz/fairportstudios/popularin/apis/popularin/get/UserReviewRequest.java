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
import xyz.fairportstudios.popularin.preferences.Auth;

public class UserReviewRequest {
    private Context context;
    private String id;
    private List<UserReview> userReviewList;
    private RecyclerView recyclerView;

    public UserReviewRequest(
            Context context,
            String id,
            List<UserReview> userReviewList,
            RecyclerView recyclerView
    ) {
        this.context = context;
        this.id = id;
        this.userReviewList = userReviewList;
        this.recyclerView = recyclerView;
    }

    public interface APICallback {
        void onSuccess();

        void onEmpty();

        void onError();
    }

    public String getRequestURL(Integer page) {
        return PopularinAPI.USER + "/" + id + "/reviews?page=" + page;
    }

    public void sendRequest(String requestURL, final APICallback callback) {
        // Auth
        final boolean isSelf = id.equals(new Auth(context).getAuthID());

        JsonObjectRequest userReview = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONArray dataArray = resultObject.getJSONArray("data");

                        for (int index = 0; index < dataArray.length(); index++) {
                            JSONObject indexObject = dataArray.getJSONObject(index);
                            JSONObject filmObject = indexObject.getJSONObject("film");

                            UserReview userReview = new UserReview();
                            userReview.setId(indexObject.getInt("id"));
                            userReview.setTmdb_id(filmObject.getInt("tmdb_id"));
                            userReview.setRating(indexObject.getDouble("rating"));
                            userReview.setReview_detail(indexObject.getString("review_detail"));
                            userReview.setTimestamp(indexObject.getString("timestamp"));
                            userReview.setTitle(filmObject.getString("title"));
                            userReview.setRelease_date(filmObject.getString("release_date"));
                            userReview.setPoster(filmObject.getString("poster"));
                            userReviewList.add(userReview);
                        }

                        UserReviewAdapter userReviewAdapter = new UserReviewAdapter(context, userReviewList, isSelf);
                        recyclerView.setAdapter(userReviewAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setVisibility(View.VISIBLE);
                        callback.onSuccess();
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

        Volley.newRequestQueue(context).add(userReview);
    }
}
