package xyz.fairportstudios.popularin.apis.popularin.get;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.statics.PopularinAPI;

public class UserWatchlistRequest {
    private Context context;
    private Integer id;

    public UserWatchlistRequest(Context context, Integer id) {
        this.context = context;
        this.id = id;
    }

    public interface Callback {
        void onSuccess(Integer pages, List<Film> films);

        void onNotFound();

        void onError(String message);
    }

    public void sendRequest(Integer page, final Callback callback) {
        String requestURL = PopularinAPI.USER + id + "/watchlists?page=" + page;

        JsonObjectRequest userWatchlist = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        List<Film> filmList = new ArrayList<>();
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONArray dataArray = resultObject.getJSONArray("data");
                        Integer totalPage = resultObject.getInt("to");

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

                        callback.onSuccess(totalPage, filmList);
                    } else if (status == 606) {
                        callback.onNotFound();
                    } else {
                        callback.onError(context.getString(R.string.general_error));
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
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("API-Key", PopularinAPI.API_KEY);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(userWatchlist);
    }
}
