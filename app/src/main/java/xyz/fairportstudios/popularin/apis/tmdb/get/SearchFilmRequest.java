package xyz.fairportstudios.popularin.apis.tmdb.get;

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

import xyz.fairportstudios.popularin.adapters.FilmAdapter;
import xyz.fairportstudios.popularin.statics.TMDbAPI;
import xyz.fairportstudios.popularin.models.Film;

public class SearchFilmRequest {
    private Context context;
    private List<Film> filmList;
    private RecyclerView recyclerView;

    public SearchFilmRequest(Context context, List<Film> filmList, RecyclerView recyclerView) {
        this.context = context;
        this.filmList = filmList;
        this.recyclerView = recyclerView;
    }

    public interface APICallback {
        void onSuccess();

        void onEmpty();

        void onError();
    }

    public void sendRequest(String query, final APICallback callback) {
        String requestURL = TMDbAPI.SEARCH_FILM + query;

        JsonObjectRequest searchFilm = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int total = response.getInt("total_results");

                    if (total > 0) {
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

                        if (filmList.size() > 0) {
                            FilmAdapter filmAdapter = new FilmAdapter(context, filmList);
                            recyclerView.setAdapter(filmAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            recyclerView.setVisibility(View.VISIBLE);
                            callback.onSuccess();
                        } else {
                            callback.onEmpty();
                        }
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

        Volley.newRequestQueue(context).add(searchFilm);
    }
}
