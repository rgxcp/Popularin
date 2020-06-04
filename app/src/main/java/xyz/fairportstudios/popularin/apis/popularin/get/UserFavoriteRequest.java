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
import xyz.fairportstudios.popularin.statics.PopularinAPI;
import xyz.fairportstudios.popularin.models.Film;

public class UserFavoriteRequest {
    private Context context;
    private String id;
    private List<Film> filmList;
    private RecyclerView recyclerView;

    public UserFavoriteRequest(
            Context context,
            String id,
            List<Film> filmList,
            RecyclerView recyclerView
    ) {
        this.context = context;
        this.id = id;
        this.filmList = filmList;
        this.recyclerView = recyclerView;
    }

    public interface JSONCallback {
        void onSuccess();

        void onEmpty();

        void onError();
    }

    public String getRequestURL(Integer page) {
        return PopularinAPI.USER + "/" + id + "/favorites?page=" + page;
    }

    public void sendRequest(String requestURL, final JSONCallback callback) {
        JsonObjectRequest userFavorite = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONArray dataArray = resultObject.getJSONArray("data");

                        for (int index = 0; index < dataArray.length(); index++) {
                            JSONObject indexObject = dataArray.getJSONObject(index);
                            JSONObject filmObject = indexObject.getJSONObject("film");

                            Film film = new Film();
                            film.setId(filmObject.getInt("tmdb_id"));
                            film.setGenre_id(filmObject.getInt("genre_id"));
                            film.setOriginal_title(filmObject.getString("title"));
                            film.setRelease_date(filmObject.getString("release_date"));
                            film.setPoster_path(filmObject.getString("poster"));
                            filmList.add(film);
                        }

                        FilmAdapter filmAdapter = new FilmAdapter(context, filmList);
                        recyclerView.setAdapter(filmAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setVisibility(View.VISIBLE);
                        callback.onSuccess();
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

        Volley.newRequestQueue(context).add(userFavorite);
    }
}
