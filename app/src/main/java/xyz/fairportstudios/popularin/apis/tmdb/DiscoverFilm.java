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

import xyz.fairportstudios.popularin.adapters.FilmListAdapter;
import xyz.fairportstudios.popularin.models.FilmList;

public class DiscoverFilm {
    private Context context;
    private List<FilmList> filmLists;
    private RecyclerView recyclerView;

    public DiscoverFilm(Context context, List<FilmList> filmLists, RecyclerView recyclerView) {
        this.context = context;
        this.filmLists = filmLists;
        this.recyclerView = recyclerView;
    }

    public String getRequestURL(String genreID, Integer page) {
        return TMDB.BASE_REQUEST
                + "/discover/movie?api_key="
                + TMDB.API_KEY
                + "&sort_by=popularity.desc&"
                + "page="
                + page
                + "&release_date.gte=2000-01-01&with_genres="
                + genreID
                + "&with_original_language=id";
    }

    public void parseJSON(String requestURL) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");

                    for (int position = 0; position < jsonArray.length(); position++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(position);

                        Integer filmID = jsonObject.getInt("id");
                        String filmTitle = jsonObject.getString("original_title");
                        String filmPoster = jsonObject.getString("poster_path");
                        String filmLanguage = jsonObject.getString("original_language");

                        if (filmLanguage.equals("id")) {
                            FilmList filmList = new FilmList(filmID, filmTitle, filmPoster);
                            filmList.setId(filmID);
                            filmList.setOriginal_title(filmTitle);
                            filmList.setPoster_path(filmPoster);

                            filmLists.add(filmList);
                        } else {
                            Log.i("Skipped", filmTitle);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                FilmListAdapter filmListAdapter = new FilmListAdapter(context, filmLists);
                recyclerView.setAdapter(filmListAdapter);
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
