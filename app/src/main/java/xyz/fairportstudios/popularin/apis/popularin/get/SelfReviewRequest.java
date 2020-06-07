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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.fairportstudios.popularin.adapters.FilmReviewAdapter;
import xyz.fairportstudios.popularin.statics.PopularinAPI;
import xyz.fairportstudios.popularin.models.FilmReview;
import xyz.fairportstudios.popularin.preferences.Auth;

public class SelfReviewRequest {
    private Context context;
    private String filmID;
    private List<FilmReview> filmReviewList;
    private RecyclerView recyclerView;

    public SelfReviewRequest(
            Context context,
            String filmID,
            List<FilmReview> filmReviewList,
            RecyclerView recyclerView
    ) {
        this.context = context;
        this.filmID = filmID;
        this.filmReviewList = filmReviewList;
        this.recyclerView = recyclerView;
    }

    public interface APICallback {
        void onSuccess(Integer lastPage);

        void onEmpty();

        void onError();
    }

    public void sendRequest(Integer page, final APICallback callback) {
        String requestURL = PopularinAPI.FILM + "/" + filmID + "/reviews/self?page=" + page;

        JsonObjectRequest selfReview = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONArray dataArray = resultObject.getJSONArray("data");
                        int lastPage = resultObject.getInt("last_page");

                        for (int index = 0; index < dataArray.length(); index++) {
                            JSONObject indexObject = dataArray.getJSONObject(index);
                            JSONObject userObject = indexObject.getJSONObject("user");

                            FilmReview filmReview = new FilmReview();
                            filmReview.setReview_id(indexObject.getInt("id"));
                            filmReview.setUser_id(userObject.getInt("id"));
                            filmReview.setRating(indexObject.getDouble("rating"));
                            filmReview.setReview_detail(indexObject.getString("review_detail"));
                            filmReview.setTimestamp(indexObject.getString("timestamp"));
                            filmReview.setUsername(userObject.getString("username"));
                            filmReview.setProfile_picture(userObject.getString("profile_picture"));
                            filmReviewList.add(filmReview);
                        }

                        FilmReviewAdapter filmReviewAdapter = new FilmReviewAdapter(context, filmReviewList);
                        recyclerView.setAdapter(filmReviewAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setVisibility(View.VISIBLE);
                        callback.onSuccess(lastPage);
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
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("API-Token", PopularinAPI.API_TOKEN);
                headers.put("Auth-Token", new Auth(context).getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(selfReview);
    }
}
