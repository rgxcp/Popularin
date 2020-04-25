package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.tmdb.GetFilmDetail;
import xyz.fairportstudios.popularin.apis.tmdb.TMDB;
import xyz.fairportstudios.popularin.fragments.FilmStatusFragment;

public class FilmDetailActivity extends AppCompatActivity {
    private FloatingActionButton fabFilmStatus;
    private ImageView imgBackdrop, imgPoster;
    private TextView txtTitle, txtYear, txtRuntime, txtOverview;
    private String filmTitle, filmYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        // Key
        Bundle bundle = getIntent().getExtras();
        String filmID = Objects.requireNonNull(bundle).getString("FILM_ID");

        // Object
        GetFilmDetail getFilmDetail = new GetFilmDetail();
        String requestURL = getFilmDetail.getRequestURL(filmID);

        // Parsing
        getFilmDetail(requestURL);
    }

    private void getFilmDetail(String requestURL) {
        JsonObjectRequest filmDetailRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Binding
                    fabFilmStatus = findViewById(R.id.fab_afd);
                    imgBackdrop = findViewById(R.id.img_afd_backdrop);
                    imgPoster = findViewById(R.id.img_afd_poster);
                    txtTitle = findViewById(R.id.txt_afd_title);
                    txtYear = findViewById(R.id.txt_afd_year);
                    txtRuntime = findViewById(R.id.txt_afd_runtime);
                    txtOverview = findViewById(R.id.txt_afd_overview);

                    // Setter
                    filmTitle = response.getString("original_title");
                    filmYear = response.getString("release_date");
                    txtTitle.setText(response.getString("original_title"));
                    txtYear.setText(response.getString("release_date"));
                    txtRuntime.setText(response.getString("runtime"));
                    txtOverview.setText(response.getString("overview"));
                    RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimaryDark).error(R.color.colorPrimaryDark);
                    Glide.with(FilmDetailActivity.this).load(TMDB.BASE_IMAGE + response.getString("backdrop_path")).apply(requestOptions).into(imgBackdrop);
                    Glide.with(FilmDetailActivity.this).load(TMDB.BASE_IMAGE + response.getString("poster_path")).apply(requestOptions).into(imgPoster);

                    // Action
                    fabFilmStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FilmStatusFragment filmStatusFragment = new FilmStatusFragment(filmTitle, filmYear);
                            filmStatusFragment.show(getSupportFragmentManager(), "FILM_STATUS_MODAL");
                        }
                    });
                } catch (Exception error) {
                    error.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(filmDetailRequest);
    }
}
