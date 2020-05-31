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

        void onEmpty();

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        String requestURL = PopularinAPI.FILM + "/" + id;

        JsonObjectRequest filmMetadata = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONObject metadataObject = resultObject.getJSONObject("metadata");

                        FilmMetadata filmMetadata = new FilmMetadata();
                        filmMetadata.setAverage_rating(metadataObject.getDouble("average_rating"));
                        filmMetadata.setTotal_favorite(metadataObject.getInt("total_favorite"));
                        filmMetadata.setTotal_review(metadataObject.getInt("total_review"));
                        filmMetadata.setTotal_watchlist(metadataObject.getInt("total_watchlist"));
                        callback.onSuccess(filmMetadata);
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

        Volley.newRequestQueue(context).add(filmMetadata);
    }
}
