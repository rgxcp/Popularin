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
import xyz.fairportstudios.popularin.models.CreditDetail;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.secrets.APIKey;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class CreditDetailRequest {
    private Context mContext;
    private int mCreditID;

    public CreditDetailRequest(Context context, int creditID) {
        mContext = context;
        mCreditID = creditID;
    }

    public interface Callback {
        void onSuccess(CreditDetail creditDetail, List<Film> filmAsCastList, List<Film> filmAsCrewList);

        void onError(String message);
    }

    public void sendRequest(final Callback callback) {
        String requestURL = TMDbAPI.CREDIT
                + mCreditID
                + "?api_key="
                + APIKey.TMDB_API_KEY
                + "&language=id&append_to_response=movie_credits";

        JsonObjectRequest creditDetail = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject movieCreditObject = response.getJSONObject("movie_credits");
                    JSONArray castArray = movieCreditObject.getJSONArray("cast");
                    JSONArray crewArray = movieCreditObject.getJSONArray("crew");

                    // Bio
                    CreditDetail creditDetail = new CreditDetail(
                            response.getString("name"),
                            response.getString("known_for_department"),
                            response.getString("birthday"),
                            response.getString("place_of_birth"),
                            response.getString("profile_path")
                    );

                    // Cast
                    List<Film> filmAsCastList = new ArrayList<>();
                    if (!castArray.isNull(0)) {
                        for (int index = 0; index < castArray.length(); index++) {
                            JSONObject indexObject = castArray.getJSONObject(index);
                            String language = indexObject.getString("original_language");

                            if (language.equals("id")) {
                                JSONArray genreArray = indexObject.getJSONArray("genre_ids");
                                int genreID = 0;
                                if (!genreArray.isNull(0)) {
                                    genreID = genreArray.getInt(0);
                                }

                                Film film = new Film(
                                        indexObject.getInt("id"),
                                        genreID,
                                        indexObject.getString("original_title"),
                                        indexObject.getString("release_date"),
                                        indexObject.getString("poster_path")
                                );

                                filmAsCastList.add(film);
                            }
                        }
                    }

                    // Crew
                    List<Film> filmAsCrewList = new ArrayList<>();
                    if (!crewArray.isNull(0)) {
                        for (int index = 0; index < crewArray.length(); index++) {
                            JSONObject indexObject = crewArray.getJSONObject(index);
                            String language = indexObject.getString("original_language");

                            if (language.equals("id")) {
                                JSONArray genreArray = indexObject.getJSONArray("genre_ids");
                                int genreID = 0;
                                if (!genreArray.isNull(0)) {
                                    genreID = genreArray.getInt(0);
                                }

                                Film film = new Film(
                                        indexObject.getInt("id"),
                                        genreID,
                                        indexObject.getString("original_title"),
                                        indexObject.getString("release_date"),
                                        indexObject.getString("poster_path")
                                );

                                filmAsCrewList.add(film);
                            }
                        }
                    }

                    callback.onSuccess(creditDetail, filmAsCastList, filmAsCrewList);
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

        Volley.newRequestQueue(mContext).add(creditDetail);
    }
}
