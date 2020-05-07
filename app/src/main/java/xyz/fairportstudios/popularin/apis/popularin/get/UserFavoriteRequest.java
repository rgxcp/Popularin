package xyz.fairportstudios.popularin.apis.popularin.get;

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
import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.models.Film;

public class UserFavoriteRequest {
    private String id;
    private Context context;
    private List<Film> filmList;
    private RecyclerView recyclerView;

    public UserFavoriteRequest(String id, Context context, List<Film> filmList, RecyclerView recyclerView) {
        this.id = id;
        this.context = context;
        this.filmList = filmList;
        this.recyclerView = recyclerView;
    }

    public interface JSONCallback {
        void onSuccess();
        void onEmptyFavorite();
    }

    public void sendRequest(final JSONCallback callback) {
        String requestURL = PopularinAPI.USER + "/" + id + "/favorites";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONArray jsonArrayData = response.getJSONObject("result").getJSONArray("data");

                        for (int index = 0; index < jsonArrayData.length(); index++) {
                            JSONObject jsonObject = jsonArrayData.getJSONObject(index);
                            JSONObject jsonObjectFilm = jsonObject.getJSONObject("film");

                            Integer id = jsonObjectFilm.getInt("tmdb_id");
                            Integer genreID = jsonObjectFilm.getInt("genre_id");
                            String title = jsonObjectFilm.getString("title");
                            String poster = jsonObjectFilm.getString("poster");
                            String releaseDate = jsonObjectFilm.getString("release_date");

                            Film film = new Film(id, genreID, title, poster, releaseDate);
                            film.setId(id);
                            film.setGenre_ids(genreID);
                            film.setOriginal_title(title);
                            film.setPoster_path(poster);
                            film.setRelease_date(releaseDate);

                            filmList.add(film);
                        }

                        FilmAdapter filmAdapter = new FilmAdapter(context, filmList);
                        recyclerView.setAdapter(filmAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setHasFixedSize(false);
                        recyclerView.setVisibility(View.VISIBLE);
                        callback.onSuccess();
                    } else {
                        callback.onEmptyFavorite();
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
