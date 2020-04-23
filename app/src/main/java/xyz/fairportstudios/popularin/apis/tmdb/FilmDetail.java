package xyz.fairportstudios.popularin.apis.tmdb;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import xyz.fairportstudios.popularin.R;

public class FilmDetail {
    private Context context;
    private ImageView backdrop;
    private ImageView poster;
    private TextView title;
    private TextView year;
    private TextView runtime;
    private TextView overview;

    public FilmDetail(Context context, ImageView backdrop, ImageView poster, TextView title, TextView year, TextView runtime, TextView overview) {
        this.context = context;
        this.backdrop = backdrop;
        this.poster = poster;
        this.title = title;
        this.year = year;
        this.runtime = runtime;
        this.overview = overview;
    }

    public String getRequestURL(String filmID) {
        return TMDB.BASE_REQUEST
                + "/movie/"
                + filmID
                + "?api_key="
                + TMDB.API_KEY
                + "&language=en-US&append_to_response=credits";
    }

    public void parseJSON(String requestURL) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    TMDB tmdb = new TMDB();
                    String release_date = tmdb.getYear(response.getString("release_date"));

                    title.setText(response.getString("title"));
                    year.setText(release_date);
                    runtime.setText(response.getString("runtime"));
                    overview.setText(response.getString("overview"));

                    RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimaryDark).error(R.color.colorPrimaryDark);
                    Glide.with(context).load(TMDB.BASE_IMAGE + response.getString("backdrop_path")).apply(requestOptions).into(backdrop);
                    Glide.with(context).load(TMDB.BASE_IMAGE + response.getString("poster_path")).apply(requestOptions).into(poster);
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
}
