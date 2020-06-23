package xyz.fairportstudios.popularin.apis.tmdb.get;

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
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.Cast;
import xyz.fairportstudios.popularin.models.Crew;
import xyz.fairportstudios.popularin.models.FilmDetail;
import xyz.fairportstudios.popularin.secrets.APIKey;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class FilmDetailRequest {
    private Context mContext;
    private int mFilmID;

    public FilmDetailRequest(Context context, int FilmID) {
        mContext = context;
        mFilmID = FilmID;
    }

    public interface Callback {
        void onSuccess(FilmDetail filmDetail, List<Cast> castList, List<Crew> crewList);

        void onError(String message);
    }

    public void sendRequest(final Callback callback) {
        String requestURL = TMDbAPI.FILM
                + mFilmID
                + "?api_key="
                + APIKey.TMDB_API_KEY
                + "&language=id&append_to_response=credits%2Cvideos";

        JsonObjectRequest filmDetail = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject creditObject = response.getJSONObject("credits");
                    JSONObject videoObject = response.getJSONObject("videos");
                    JSONArray castArray = creditObject.getJSONArray("cast");
                    JSONArray crewArray = creditObject.getJSONArray("crew");
                    JSONArray videoArray = videoObject.getJSONArray("results");

                    // Detail
                    FilmDetail filmDetail = new FilmDetail(
                            response.getJSONArray("genres").getInt(0),
                            response.getInt("runtime"),
                            response.getString("original_title"),
                            response.getString("release_date"),
                            response.getString("overview"),
                            response.getString("poster_path"),
                            videoArray.getJSONObject(0).getString("key")
                    );

                    // Cast
                    List<Cast> castList = new ArrayList<>();
                    if (castArray.length() != 0) {
                        for (int index = 0; index < castArray.length(); index++) {
                            JSONObject indexObject = castArray.getJSONObject(index);

                            Cast cast = new Cast(
                                    indexObject.getInt("id"),
                                    indexObject.getString("name"),
                                    indexObject.getString("character"),
                                    indexObject.getString("profile_path")
                            );

                            castList.add(cast);
                        }
                    }

                    // Crew
                    List<Crew> crewList = new ArrayList<>();
                    if (crewArray.length() != 0) {
                        for (int index = 0; index < crewArray.length(); index++) {
                            JSONObject indexObject = crewArray.getJSONObject(index);

                            Crew crew = new Crew(
                                    indexObject.getInt("id"),
                                    indexObject.getString("name"),
                                    indexObject.getString("job"),
                                    indexObject.getString("profile_path")
                            );

                            crewList.add(crew);
                        }
                    }

                    callback.onSuccess(filmDetail, castList, crewList);
                } catch (JSONException exception) {
                    exception.printStackTrace();
                    callback.onError(mContext.getString(R.string.general_error));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof NetworkError || error instanceof TimeoutError) {
                    callback.onError(mContext.getString(R.string.network_error));
                } else if (error instanceof ServerError) {
                    callback.onError(mContext.getString(R.string.server_error));
                } else {
                    callback.onError(mContext.getString(R.string.general_error));
                }
            }
        });

        Volley.newRequestQueue(mContext).add(filmDetail);
    }
}
