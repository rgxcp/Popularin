package xyz.fairportstudios.popularin.apis.popularin.get;

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
import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;
import xyz.fairportstudios.popularin.models.Film;

public class UserFavoriteRequest {
    private Context context;
    private List<Film> filmList;
    private RecyclerView recyclerView;

    public UserFavoriteRequest(Context context, List<Film> filmList, RecyclerView recyclerView) {
        this.context = context;
        this.filmList = filmList;
        this.recyclerView = recyclerView;
    }

    public interface JSONCallback {
        void onSuccess();

        void onEmpty();

        void onError();
    }

    public String getRequestURL(String id, Integer page) {
        return PopularinAPI.USER
                + "/"
                + id
                + "/favorites?page="
                + page;
    }

    public void sendRequest(String requestURL, final JSONCallback callback) {
        JsonObjectRequest userFavoriteRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONArray jsonArrayData = response.getJSONObject("result").getJSONArray("data");

                        for (int index = 0; index < jsonArrayData.length(); index++) {
                            JSONObject jsonObject = jsonArrayData.getJSONObject(index);
                            JSONObject jsonObjectFilm = jsonObject.getJSONObject("film");

                            Film film = new Film();
                            film.setId(jsonObjectFilm.getInt("tmdb_id"));
                            film.setGenre_ids(jsonObjectFilm.getInt("genre_id"));
                            film.setOriginal_title(jsonObjectFilm.getString("title"));
                            film.setPoster_path(jsonObjectFilm.getString("poster"));
                            film.setRelease_date(jsonObjectFilm.getString("release_date"));

                            filmList.add(film);
                        }

                        FilmAdapter filmAdapter = new FilmAdapter(context, filmList);
                        recyclerView.setAdapter(filmAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setHasFixedSize(false);
                        recyclerView.setVisibility(View.VISIBLE);
                        callback.onSuccess();
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

        Volley.newRequestQueue(context).add(userFavoriteRequest);
    }
}
