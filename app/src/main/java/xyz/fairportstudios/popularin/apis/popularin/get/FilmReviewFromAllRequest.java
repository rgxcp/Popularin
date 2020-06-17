package xyz.fairportstudios.popularin.apis.popularin.get;

import android.content.Context;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.FilmReview;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.statics.PopularinAPI;

public class FilmReviewFromAllRequest {
    private Context context;
    private Integer id;

    public FilmReviewFromAllRequest(Context context, Integer id) {
        this.context = context;
        this.id = id;
    }

    public interface Callback {
        void onSuccess(Integer totalPage, List<FilmReview> filmReviewList);

        void onNotFound();

        void onError(String message);
    }

    public void sendRequest(Integer page, final Callback callback) {
        String requestURL = PopularinAPI.FILM + id + "/reviews/from/all?page=" + page;

        JsonObjectRequest filmReviewFromAll = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        List<FilmReview> filmReviewList = new ArrayList<>();
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONArray dataArray = resultObject.getJSONArray("data");
                        Integer totalPage = resultObject.getInt("last_page");

                        for (int index = 0; index < dataArray.length(); index++) {
                            JSONObject indexObject = dataArray.getJSONObject(index);
                            JSONObject userObject = indexObject.getJSONObject("user");

                            FilmReview filmReview = new FilmReview();
                            filmReview.setId(indexObject.getInt("id"));
                            filmReview.setUser_id(userObject.getInt("id"));
                            filmReview.setTotal_like(indexObject.getInt("total_like"));
                            filmReview.setTotal_comment(indexObject.getInt("total_comment"));
                            filmReview.setIs_liked(indexObject.getBoolean("is_liked"));
                            filmReview.setRating(indexObject.getDouble("rating"));
                            filmReview.setReview_detail(indexObject.getString("review_detail"));
                            filmReview.setTimestamp(indexObject.getString("timestamp"));
                            filmReview.setUsername(userObject.getString("username"));
                            filmReview.setProfile_picture(userObject.getString("profile_picture"));
                            filmReviewList.add(filmReview);
                        }

                        callback.onSuccess(totalPage, filmReviewList);
                    } else if (status == 606) {
                        callback.onNotFound();
                    } else {
                        callback.onError(context.getString(R.string.general_error));
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                    callback.onError(context.getString(R.string.general_error));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof NetworkError || error instanceof TimeoutError) {
                    callback.onError(context.getString(R.string.network_error));
                } else if (error instanceof ServerError) {
                    callback.onError(context.getString(R.string.server_error));
                } else {
                    callback.onError(context.getString(R.string.general_error));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("API-Key", PopularinAPI.API_KEY);
                headers.put("Auth-Token", new Auth(context).getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(filmReviewFromAll);
    }
}
