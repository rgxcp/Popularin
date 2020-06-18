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
    private Context context;
    private Integer id;

    public CreditDetailRequest(Context context, Integer id) {
        this.context = context;
        this.id = id;
    }

    public interface Callback {
        void onSuccess(CreditDetail creditDetail, List<Film> filmAsCastList, List<Film> filmAsCrewList);

        void onError(String message);
    }

    public void sendRequest(final Callback callback) {
        String requestURL = TMDbAPI.CREDIT
                + id
                + "?api_key="
                + APIKey.TMDB_API_KEY
                + "&language=id&append_to_response=movie_credits";

        JsonObjectRequest creditDetail = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    CreditDetail creditDetail = new CreditDetail();
                    creditDetail.setName(response.getString("name"));
                    creditDetail.setKnown_for_department(response.getString("known_for_department"));
                    creditDetail.setBirthday(response.getString("birthday"));
                    creditDetail.setPlace_of_birth(response.getString("place_of_birth"));
                    creditDetail.setProfile_path(response.getString("profile_path"));

                    JSONObject movieCreditObject = response.getJSONObject("movie_credits");
                    JSONArray castArray = movieCreditObject.getJSONArray("cast");
                    List<Film> filmAsCastList = new ArrayList<>();
                    if (!castArray.isNull(0)) {
                        for (int index = 0; index < castArray.length(); index++) {
                            JSONObject indexObject = castArray.getJSONObject(index);
                            String language = indexObject.getString("original_language");

                            if (language.equals("id")) {
                                Film film = new Film();
                                film.setId(indexObject.getInt("id"));
                                film.setOriginal_title(indexObject.getString("original_title"));
                                film.setRelease_date(indexObject.getString("release_date"));
                                film.setPoster_path(indexObject.getString("poster_path"));
                                filmAsCastList.add(film);
                            }
                        }
                    }

                    JSONArray crewArray = movieCreditObject.getJSONArray("crew");
                    List<Film> filmAsCrewList = new ArrayList<>();
                    if (!crewArray.isNull(0)) {
                        for (int index = 0; index < crewArray.length(); index++) {
                            JSONObject indexObject = crewArray.getJSONObject(index);
                            String language = indexObject.getString("original_language");

                            if (language.equals("id")) {
                                Film film = new Film();
                                film.setId(indexObject.getInt("id"));
                                film.setOriginal_title(indexObject.getString("original_title"));
                                film.setRelease_date(indexObject.getString("release_date"));
                                film.setPoster_path(indexObject.getString("poster_path"));
                                filmAsCrewList.add(film);
                            }
                        }
                    }

                    callback.onSuccess(creditDetail, filmAsCastList, filmAsCrewList);
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
        });

        Volley.newRequestQueue(context).add(creditDetail);
    }
}
