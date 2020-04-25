package xyz.fairportstudios.popularin.apis.tmdb;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class GetFilmDetail {
    /*
    private Context context;

    public GetFilmDetail(Context context) {
        this.context = context;
    }
     */

    public String getRequestURL(String filmID) {
        return TMDB.BASE_REQUEST
                + "/movie/"
                + filmID
                + "?api_key="
                + TMDB.API_KEY
                + "&language=en-US&append_to_response=credits";
    }

    /*
    public void parseJSON(String requestURL) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String backdrop_path = response.getString("backdrop_path");
                    String overview = response.getString("overview");
                    String poster_path = response.getString("poster_path");
                    String release_date = response.getString("release_date");
                    String runtime = response.getString("runtime");
                    String title = response.getString("title");
                } catch (JSONException error) {
                    error.printStackTrace();
                }
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
     */
}
