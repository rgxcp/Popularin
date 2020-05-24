package xyz.fairportstudios.popularin.apis.tmdb.get;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
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

import xyz.fairportstudios.popularin.adapters.FilmGridAdapter;
import xyz.fairportstudios.popularin.apis.tmdb.TMDbAPI;
import xyz.fairportstudios.popularin.models.Film;

public class DiscoverFilmRequest {
    private Context context;
    private String genreID;
    private List<Film> filmList;
    private RecyclerView recyclerView;

    public DiscoverFilmRequest(
            Context context,
            String genreID,
            List<Film> filmList,
            RecyclerView recyclerView
    ) {
        this.context = context;
        this.genreID = genreID;
        this.filmList = filmList;
        this.recyclerView = recyclerView;
    }

    public interface APICallback {
        void onSuccess();

        void onError();
    }

    public String getRequestURL(Integer page) {
        return TMDbAPI.DISCOVER_FILM
                + "?api_key="
                + TMDbAPI.API_KEY
                + "&language=id&sort_by=popularity.desc&page="
                + page
                + "&release_date.gte=2000-01-01&with_genres="
                + genreID
                + "&with_runtime.gte=0&with_original_language=id";
    }

    public void sendRequest(String requestURL, final APICallback callback) {
        JsonObjectRequest discoverFilm = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray resultArray = response.getJSONArray("results");

                    for (int index = 0; index < resultArray.length(); index++) {
                        JSONObject indexObject = resultArray.getJSONObject(index);
                        String language = indexObject.getString("original_language");

                        if (language.equals("id")) {
                            Film film = new Film();
                            film.setId(indexObject.getInt("id"));
                            film.setGenre_id(indexObject.getJSONArray("genre_ids").getInt(0));
                            film.setOriginal_title(indexObject.getString("original_title"));
                            film.setRelease_date(indexObject.getString("release_date"));
                            film.setPoster_path(indexObject.getString("poster_path"));
                            filmList.add(film);
                        }
                    }

                    FilmGridAdapter filmGridAdapter = new FilmGridAdapter(context, filmList);
                    recyclerView.setAdapter(filmGridAdapter);
                    recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
                    recyclerView.setVisibility(View.VISIBLE);
                    callback.onSuccess();
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

        Volley.newRequestQueue(context).add(discoverFilm);
    }
}
