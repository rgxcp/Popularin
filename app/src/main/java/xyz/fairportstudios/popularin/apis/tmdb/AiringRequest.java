package xyz.fairportstudios.popularin.apis.tmdb;

import android.content.Context;
import android.util.Log;

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
import xyz.fairportstudios.popularin.models.Film;

public class AiringRequest {
    private Context context;
    private List<Film> filmList;
    private RecyclerView recyclerView;

    public AiringRequest(Context context, List<Film> filmList, RecyclerView recyclerView) {
        this.context = context;
        this.filmList = filmList;
        this.recyclerView = recyclerView;
    }

    public void sendRequest() {
        String requestURL = TMDBBaseRequest.AIRING
                + "?api_key="
                + TMDBBaseRequest.API_KEY
                + "&language=id-ID&page=1&region=id";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArrayResult = response.getJSONArray("results");

                    for (int index = 0; index < jsonArrayResult.length(); index++) {
                        JSONObject jsonObject = jsonArrayResult.getJSONObject(index);

                        String language = jsonObject.getString("original_language");

                        if (language.equals("id")) {
                            Integer id = jsonObject.getInt("id");
                            Integer genre_ids = jsonObject.getJSONArray("genre_ids").getInt(0);
                            String original_title = jsonObject.getString("original_title");
                            String poster_path = jsonObject.getString("poster_path");
                            String release_date = jsonObject.getString("release_date");

                            Film film = new Film(id, genre_ids, original_title, poster_path, release_date);
                            film.setId(id);
                            film.setGenre_ids(genre_ids);
                            film.setOriginal_title(original_title);
                            film.setPoster_path(poster_path);
                            film.setRelease_date(release_date);

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
