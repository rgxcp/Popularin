package xyz.fairportstudios.popularin.apis.tmdb.get;

import android.content.Context;

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

public class DiscoverGenre {
    private Context context;
    private List<Film> filmList;
    private RecyclerView recyclerView;

    public DiscoverGenre(Context context, List<Film> filmList, RecyclerView recyclerView) {
        this.context = context;
        this.filmList = filmList;
        this.recyclerView = recyclerView;
    }

    public String getRequestURL(String genre, String page) {
        return TMDbAPI.DISCOVER_GENRE
                + "?api_key="
                + TMDbAPI.API_KEY
                + "&language=id&sort_by=popularity.desc&page="
                + page
                + "&release_date.gte=2000-01-01&with_genres="
                + genre
                + "&with_runtime.gte=0&with_original_language=id";
    }

    public void sendRequest(String requestURL) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArrayResult = response.getJSONArray("results");

                    for (int index = 0; index < jsonArrayResult.length(); index++) {
                        JSONObject jsonObject = jsonArrayResult.getJSONObject(index);

                        String language = jsonObject.getString("original_language");

                        if (language.equals("id")) {
                            Film film = new Film();
                            film.setId(jsonObject.getInt("id"));
                            film.setGenre_ids(jsonObject.getJSONArray("genre_ids").getInt(0));
                            film.setOriginal_title(jsonObject.getString("original_title"));
                            film.setPoster_path(jsonObject.getString("poster_path"));
                            film.setRelease_date(jsonObject.getString("release_date"));

                            filmList.add(film);
                        }
                    }
                } catch (JSONException error) {
                    error.printStackTrace();
                }

                FilmAdapter filmAdapter = new FilmAdapter(context, filmList);
                recyclerView.setAdapter(filmAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
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
