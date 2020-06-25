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

public class DiscoverFilmRequest {
    private Context mContext;
    private int mGenreID;

    public DiscoverFilmRequest(Context context, int genreID) {
        mContext = context;
        mGenreID = genreID;
    }

    public interface Callback {
        void onSuccess(int totalPage, List<Film> filmList);

        void onError(String message);
    }

    public void sendRequest(int page, final Callback callback) {
        String requestURL = TMDbAPI.DISCOVER
                + "?api_key="
                + APIKey.TMDB_API_KEY
                + "&language=id&region=ID&sort_by=popularity.desc&page="
                + page
                + "&release_date.gte=2000-01-01&with_genres="
                + mGenreID
                + "&with_original_language=id";

        JsonObjectRequest discoverFilm = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    List<Film> filmList = new ArrayList<>();
                    JSONArray resultArray = response.getJSONArray("results");
                    int totalPage = response.getInt("total_pages");

                    for (int index = 0; index < resultArray.length(); index++) {
                        JSONObject indexObject = resultArray.getJSONObject(index);
                        JSONArray genreArray = indexObject.getJSONArray("genre_ids");
                        int genreID = 0;
                        if (!genreArray.isNull(0)) {
                            genreID = genreArray.getInt(0);
                        }

                        Film film = new Film(
                                indexObject.getInt("id"),
                                genreID,
                                indexObject.getString("original_title"),
                                indexObject.getString("release_date"),
                                indexObject.getString("poster_path")
                        );

                        filmList.add(film);
                    }

                    callback.onSuccess(totalPage, filmList);
                } catch (JSONException exception) {
                    exception.printStackTrace();
                    callback.onError(mContext.getString(R.string.general_error));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof NetworkError || error instanceof TimeoutError) {
                    callback.onError(mContext.getString(R.string.network_error));
                } else if (error instanceof ServerError) {
                    callback.onError(mContext.getString(R.string.server_error));
                } else {
                    callback.onError(mContext.getString(R.string.general_error));
                }
            }
        });

        Volley.newRequestQueue(mContext).add(discoverFilm);
    }
}
