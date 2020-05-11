package xyz.fairportstudios.popularin.apis.popularin.get;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.models.FilmMetadata;

public class FilmMetadataRequest {
    private Context context;
    private String id;

    public FilmMetadataRequest(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    public interface APICallback {
        void onSuccess(FilmMetadata filmMetadata);

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        String requestURL = PopularinAPI.FILM + "/" + id;

        JsonObjectRequest filmMetadataRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject jsonObjectResult = response.getJSONObject("result");
                        // JSONObject jsonObjectFilm = jsonObjectResult.getJSONObject("film");
                        JSONObject jsonObjectMetadata = jsonObjectResult.getJSONObject("metadata");

                        FilmMetadata filmMetadata = new FilmMetadata();
                        filmMetadata.setAverage_rating(jsonObjectMetadata.getDouble("average_rating"));
                        filmMetadata.setFavorites(jsonObjectMetadata.getInt("favorites"));
                        filmMetadata.setReviews(jsonObjectMetadata.getInt("reviews"));
                        filmMetadata.setWatchlists(jsonObjectMetadata.getInt("watchlists"));
                        filmMetadata.setRate_05(jsonObjectMetadata.getInt("rate_0.5"));
                        filmMetadata.setRate_10(jsonObjectMetadata.getInt("rate_1.0"));
                        filmMetadata.setRate_15(jsonObjectMetadata.getInt("rate_1.5"));
                        filmMetadata.setRate_20(jsonObjectMetadata.getInt("rate_2.0"));
                        filmMetadata.setRate_25(jsonObjectMetadata.getInt("rate_2.5"));
                        filmMetadata.setRate_30(jsonObjectMetadata.getInt("rate_3.0"));
                        filmMetadata.setRate_35(jsonObjectMetadata.getInt("rate_3.5"));
                        filmMetadata.setRate_40(jsonObjectMetadata.getInt("rate_4.0"));
                        filmMetadata.setRate_45(jsonObjectMetadata.getInt("rate_4.5"));
                        filmMetadata.setRate_50(jsonObjectMetadata.getInt("rate_5.0"));
                        callback.onSuccess(filmMetadata);
                    } else {
                        FilmMetadata filmMetadata = new FilmMetadata();
                        filmMetadata.setAverage_rating(0.0);
                        filmMetadata.setFavorites(0);
                        filmMetadata.setReviews(0);
                        filmMetadata.setWatchlists(0);
                        filmMetadata.setRate_05(0);
                        filmMetadata.setRate_10(0);
                        filmMetadata.setRate_15(0);
                        filmMetadata.setRate_20(0);
                        filmMetadata.setRate_25(0);
                        filmMetadata.setRate_30(0);
                        filmMetadata.setRate_35(0);
                        filmMetadata.setRate_40(0);
                        filmMetadata.setRate_45(0);
                        filmMetadata.setRate_50(0);
                        callback.onSuccess(filmMetadata);
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

        Volley.newRequestQueue(context).add(filmMetadataRequest);
    }
}
