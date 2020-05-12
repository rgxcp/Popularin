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

import xyz.fairportstudios.popularin.adapters.FilmReviewAdapter;
import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.models.FilmReview;

public class ReviewFromAllRequest {
    private Context context;
    private List<FilmReview> filmReviewList;
    private RecyclerView recyclerView;

    public ReviewFromAllRequest(Context context, List<FilmReview> filmReviewList, RecyclerView recyclerView) {
        this.context = context;
        this.filmReviewList = filmReviewList;
        this.recyclerView = recyclerView;
    }

    public interface APICallback {
        void onSuccess();

        void onEmpty();

        void onError();
    }

    public String getRequestURL(String id, Integer page) {
        return PopularinAPI.FILM
                + "/"
                + id
                + "/reviews/from/all?page="
                + page;
    }

    public void sendRequest(String requestURL, final APICallback callback) {
        JsonObjectRequest reviewFromAll = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject jsonObjectResult = response.getJSONObject("result");
                        JSONArray jsonArrayData = jsonObjectResult.getJSONArray("data");

                        for (int index = 0; index < jsonArrayData.length(); index++) {
                            JSONObject jsonObject = jsonArrayData.getJSONObject(index);
                            JSONObject jsonObjectUser = jsonObject.getJSONObject("user");

                            FilmReview filmReview = new FilmReview();
                            filmReview.setId(jsonObject.getInt("id"));
                            filmReview.setUser_id(jsonObjectUser.getInt("id"));
                            filmReview.setRating(jsonObject.getDouble("rating"));
                            filmReview.setReview_text(jsonObject.getString("review_text"));
                            filmReview.setReview_date(jsonObject.getString("review_date"));
                            filmReview.setFirst_name(jsonObjectUser.getString("first_name"));
                            filmReview.setProfile_picture(jsonObjectUser.getString("profile_picture"));

                            filmReviewList.add(filmReview);
                        }

                        FilmReviewAdapter filmReviewAdapter = new FilmReviewAdapter(context, filmReviewList);
                        recyclerView.setAdapter(filmReviewAdapter);
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

        Volley.newRequestQueue(context).add(reviewFromAll);
    }
}