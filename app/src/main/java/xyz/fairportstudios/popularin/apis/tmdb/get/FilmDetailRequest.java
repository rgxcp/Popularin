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
    private List<Cast> castList;
    private List<Crew> crewList;
    private RecyclerView recyclerCast;
    private RecyclerView recyclerCrew;
    private String id;

    public FilmDetailRequest(Context context, List<Cast> castList, List<Crew> crewList, RecyclerView recyclerCast, RecyclerView recyclerCrew, String id) {
        this.context = context;
        this.castList = castList;
        this.crewList = crewList;
        this.recyclerCast = recyclerCast;
        this.recyclerCrew = recyclerCrew;
        this.id = id;
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
                + "&language=id&append_to_response=credits%2Cimages";

        JsonObjectRequest filmDetailRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    FilmDetail filmDetail = new FilmDetail();
                    filmDetail.setRuntime(response.getInt("runtime"));
                    filmDetail.setBackdrop_path(response.getString("backdrop_path"));
                    filmDetail.setGenre(response.getJSONArray("genres").getJSONObject(0).getString("name"));
                    filmDetail.setOriginal_title(response.getString("original_title"));
                    filmDetail.setOverview(response.getString("overview"));
                    filmDetail.setPoster_path(response.getString("poster_path"));
                    filmDetail.setRelease_date(response.getString("release_date"));
                    callback.onSuccess(filmDetail);

                    JSONObject jsonObjectCredits = response.getJSONObject("credits");
                    JSONArray jsonArrayCast = jsonObjectCredits.getJSONArray("cast");
                    JSONArray jsonArrayCrew = jsonObjectCredits.getJSONArray("crew");

                    getCast(jsonArrayCast);
                    getCrew(jsonArrayCrew);
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

        Volley.newRequestQueue(context).add(filmDetailRequest);
    }

    private void getCast(JSONArray jsonArrayCast) {
        try {
            for (int index = 0; index < jsonArrayCast.length(); index++) {
                JSONObject jsonObject = jsonArrayCast.getJSONObject(index);

                Cast cast = new Cast();
                cast.setId(jsonObject.getInt("id"));
                cast.setGender(jsonObject.getInt("gender"));
                cast.setCharacter(jsonObject.getString("character"));
                cast.setName(jsonObject.getString("name"));
                cast.setProfile_path(jsonObject.getString("profile_path"));

                castList.add(cast);
            }

            CastAdapter castAdapter = new CastAdapter(context, castList);
            recyclerCast.setAdapter(castAdapter);
            recyclerCast.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            recyclerCast.setVisibility(View.VISIBLE);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    private void getCrew(JSONArray jsonArrayCrew) {
        try {
            for (int index = 0; index < jsonArrayCrew.length(); index++) {
                JSONObject jsonObject = jsonArrayCrew.getJSONObject(index);

                Crew crew = new Crew();
                crew.setId(jsonObject.getInt("id"));
                crew.setGender(jsonObject.getInt("gender"));
                crew.setJob(jsonObject.getString("job"));
                crew.setName(jsonObject.getString("name"));
                crew.setProfile_path(jsonObject.getString("profile_path"));

                crewList.add(crew);
            }

            CrewAdapter crewAdapter = new CrewAdapter(context, crewList);
            recyclerCrew.setAdapter(crewAdapter);
            recyclerCrew.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            recyclerCrew.setVisibility(View.VISIBLE);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
}
