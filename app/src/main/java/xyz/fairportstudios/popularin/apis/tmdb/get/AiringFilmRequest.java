package xyz.fairportstudios.popularin.apis.tmdb.get;

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
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.secrets.APIKey;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class AiringFilmRequest {
    private Context context;

    public AiringFilmRequest(Context context) {
        this.context = context;
    }

    public interface Callback {
        void onSuccess(List<Film> films);

        void onNotFound();

        void onError(String message);
    }

    public void sendRequest(final Callback callback) {
        String requestURL = TMDbAPI.AIRING
                + "?api_key="
                + APIKey.TMDB_API_KEY
                + "&language=id&region=ID";

        JsonObjectRequest airingFilm = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int totalResult = response.getInt("total_results");

                    if (totalResult > 0) {
                        List<Film> filmList = new ArrayList<>();
                        JSONArray resultArray = response.getJSONArray("results");

                        for (int index = 0; index < resultArray.length(); index++) {
                            JSONObject indexObject = resultArray.getJSONObject(index);
                            String language = indexObject.getString("original_language");

                            if (language.equals("id")) {
                                Film film = new Film(
                                        indexObject.getInt("id"),
                                        indexObject.getJSONArray("genre_ids").getInt(0),
                                        indexObject.getString("original_title"),
                                        indexObject.getString("release_date"),
                                        indexObject.getString("poster_path")
                                );
                                filmList.add(film);
                            }
                        }

                        if (filmList.size() > 0) {
                            callback.onSuccess(filmList);
                        } else {
                            callback.onNotFound();
                        }
                    } else {
                        callback.onNotFound();
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
        });

        Volley.newRequestQueue(context).add(airingFilm);
    }
}
