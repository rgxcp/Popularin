package xyz.fairportstudios.popularin.apis.tmdb.get;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import xyz.fairportstudios.popularin.adapters.FilmAdapter;
import xyz.fairportstudios.popularin.apis.tmdb.TMDbAPI;
import xyz.fairportstudios.popularin.models.Film;

public class SearchFilm {
    private Context context;
    private List<Film> filmList;
    private RecyclerView recyclerView;

    public SearchFilm(Context context, List<Film> filmList, RecyclerView recyclerView) {
        this.context = context;
        this.filmList = filmList;
        this.recyclerView = recyclerView;
    }

    public interface JSONCallback {
        void onSuccess();
        void onEmptyResult();
        void onEmptyIndonesian();
    }

    public String getRequestURL(String query, String page) {
        return TMDbAPI.SEARCH_FILM
                + "?api_key="
                + TMDbAPI.API_KEY
                + "&language=id&query="
                + query
                + "&page="
                + page;
    }

    public void sendRequest(String requestURL, final JSONCallback callback) {
        // Membersihkan sebelum mencari
        filmList.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int totalResults = response.getInt("total_results");

                    if (totalResults > 0) {
                        JSONArray jsonArrayResults = response.getJSONArray("results");

                        for (int index = 0; index < jsonArrayResults.length(); index++) {
                            JSONObject jsonObject = jsonArrayResults.getJSONObject(index);
                            String language = jsonObject.getString("original_language");

                            if (language.equals("id")) {
                                Film film = new Film();
                                film.setId(jsonObject.getInt("id"));
                                film.setGenre_ids(jsonObject.getJSONArray("genre_ids").getInt(0));
                                film.setOriginal_title(jsonObject.getString("original_title"));
                                film.setPoster_path(jsonObject.getString("poster_path"));
                                film.setRelease_date(jsonObject.getString("release_date"));

                                filmList.add(film);
                            } else {
                                callback.onEmptyIndonesian();
                            }
                        }

                        FilmAdapter filmAdapter = new FilmAdapter(context, filmList);
                        recyclerView.setAdapter(filmAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setVisibility(View.VISIBLE);
                        callback.onSuccess();
                    } else {
                        callback.onEmptyResult();
                    }
                } catch (JSONException error) {
                    error.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }
}
