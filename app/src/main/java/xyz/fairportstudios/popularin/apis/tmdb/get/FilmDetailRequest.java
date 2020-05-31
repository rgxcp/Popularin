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

import xyz.fairportstudios.popularin.adapters.CastAdapter;
import xyz.fairportstudios.popularin.adapters.CrewAdapter;
import xyz.fairportstudios.popularin.apis.tmdb.TMDbAPI;
import xyz.fairportstudios.popularin.models.Cast;
import xyz.fairportstudios.popularin.models.Crew;
import xyz.fairportstudios.popularin.models.FilmDetail;

public class FilmDetailRequest {
    private Context context;
    private String id;
    private List<Cast> castList;
    private List<Crew> crewList;
    private RecyclerView recyclerCast;
    private RecyclerView recyclerCrew;

    public FilmDetailRequest(
            Context context,
            String id,
            List<Cast> castList,
            List<Crew> crewList,
            RecyclerView recyclerCast,
            RecyclerView recyclerCrew
    ) {
        this.context = context;
        this.id = id;
        this.castList = castList;
        this.crewList = crewList;
        this.recyclerCast = recyclerCast;
        this.recyclerCrew = recyclerCrew;
    }

    public interface APICallback {
        void onSuccess(FilmDetail filmDetail);

        void onError();
    }

    public void sendRequest(final APICallback callback) {
        String requestURL = TMDbAPI.FILM_DETAIL
                + id
                + "?api_key="
                + TMDbAPI.API_KEY
                + "&language=id&append_to_response=credits%2Cvideos";

        JsonObjectRequest filmDetail = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject creditObject = response.getJSONObject("credits");
                    JSONArray castArray = creditObject.getJSONArray("cast");
                    JSONArray crewArray = creditObject.getJSONArray("crew");
                    JSONArray arrayVideo = response.getJSONObject("videos").getJSONArray("results");

                    // Detail film
                    FilmDetail filmDetail = new FilmDetail();
                    filmDetail.setGenre_id(response.getJSONArray("genres").getJSONObject(0).getInt("id"));
                    filmDetail.setRuntime(response.getInt("runtime"));
                    filmDetail.setOriginal_title(response.getString("original_title"));
                    filmDetail.setOverview(response.getString("overview"));
                    filmDetail.setPoster_path(response.getString("poster_path"));
                    filmDetail.setRelease_date(response.getString("release_date"));
                    if (!arrayVideo.isNull(0)) {
                        filmDetail.setVideo_key(arrayVideo.getJSONObject(0).getString("key"));
                    } else {
                        filmDetail.setVideo_key("");
                    }

                    // Cast film
                    for (int index = 0; index < castArray.length(); index++) {
                        JSONObject indexObject = castArray.getJSONObject(index);

                        Cast cast = new Cast();
                        cast.setId(indexObject.getInt("id"));
                        cast.setCharacter(indexObject.getString("character"));
                        cast.setName(indexObject.getString("name"));
                        cast.setProfile_path(indexObject.getString("profile_path"));
                        castList.add(cast);
                    }
                    CastAdapter castAdapter = new CastAdapter(context, castList);
                    recyclerCast.setAdapter(castAdapter);
                    recyclerCast.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                    recyclerCast.setVisibility(View.VISIBLE);

                    // Crew film
                    for (int index = 0; index < crewArray.length(); index++) {
                        JSONObject indexObject = crewArray.getJSONObject(index);

                        Crew crew = new Crew();
                        crew.setId(indexObject.getInt("id"));
                        crew.setJob(indexObject.getString("job"));
                        crew.setName(indexObject.getString("name"));
                        crew.setProfile_path(indexObject.getString("profile_path"));
                        crewList.add(crew);
                    }
                    CrewAdapter crewAdapter = new CrewAdapter(context, crewList);
                    recyclerCrew.setAdapter(crewAdapter);
                    recyclerCrew.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                    recyclerCrew.setVisibility(View.VISIBLE);
                    callback.onSuccess(filmDetail);
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

        Volley.newRequestQueue(context).add(filmDetail);
    }
}
