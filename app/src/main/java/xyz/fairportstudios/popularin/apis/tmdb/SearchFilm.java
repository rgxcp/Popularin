package xyz.fairportstudios.popularin.apis.tmdb;

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

import xyz.fairportstudios.popularin.adapters.FilmListAdapter;
import xyz.fairportstudios.popularin.models.FilmList;

public class SearchFilm {
    private List<FilmList> filmLists;
    private RecyclerView recyclerView;

    public SearchFilm(List<FilmList> filmLists, RecyclerView recyclerView) {
        this.filmLists = filmLists;
        this.recyclerView = recyclerView;
    }

    public void parseJSON(String requestURL, final Context context) {
        filmLists.clear();

        JsonObjectRequest mJSONObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray mJSONArray = response.getJSONArray("results");

                    for (int position = 0; position < mJSONArray.length(); position++) {
                        JSONObject mJSONObject = mJSONArray.getJSONObject(position);

                        Integer mID = mJSONObject.getInt("id");
                        String mOriginalTitle = mJSONObject.getString("original_title");
                        String mPosterPath = mJSONObject.getString("poster_path");

                        FilmList filmList = new FilmList(mID, mOriginalTitle, mPosterPath);
                        filmList.setId(mID);
                        filmList.setOriginal_title(mOriginalTitle);
                        filmList.setPoster_path(mPosterPath);

                        filmLists.add(filmList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                FilmListAdapter mFilmListAdapter = new FilmListAdapter(context, filmLists);
                recyclerView.setAdapter(mFilmListAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        mRequestQueue.add(mJSONObjectRequest);
    }
}
