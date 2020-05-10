package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.tmdb.get.FilmDetailRequest;
import xyz.fairportstudios.popularin.models.Cast;
import xyz.fairportstudios.popularin.models.Crew;
import xyz.fairportstudios.popularin.models.FilmDetail;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseImage;

public class FilmDetailActivity extends AppCompatActivity {
    private Context context;
    private ImageView filmPoster;
    private ImageView filmBackdrop;
    private ProgressBar progressBar;
    private RelativeLayout layout;
    private ScrollView scroll;
    private String title;
    private TextView filmTitle;
    private TextView filmGenre;
    private TextView filmDate;
    private TextView filmRuntime;
    private TextView filmOverview;
    private TextView emptyResult;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        // Binding
        context = FilmDetailActivity.this;
        filmPoster = findViewById(R.id.image_afd_poster);
        filmBackdrop = findViewById(R.id.image_afd_backdrop);
        progressBar = findViewById(R.id.pbr_afd_layout);
        layout = findViewById(R.id.layout_afd_anchor);
        scroll = findViewById(R.id.scroll_afd_layout);
        filmTitle = findViewById(R.id.text_afd_title);
        filmGenre = findViewById(R.id.text_afd_genre);
        filmDate = findViewById(R.id.text_afd_date);
        filmRuntime = findViewById(R.id.text_afd_runtime);
        filmOverview = findViewById(R.id.text_afd_overview);
        emptyResult = findViewById(R.id.text_afd_empty);
        toolbar = findViewById(R.id.toolbar_afd_layout);
        FloatingActionButton fab = findViewById(R.id.fab_afd_layout);
        RecyclerView recyclerCast = findViewById(R.id.recycler_cast_afd_layout);
        RecyclerView recyclerCrew = findViewById(R.id.recycler_crew_afd_layout);

        // Bundle
        Bundle bundle = getIntent().getExtras();
        String filmID = Objects.requireNonNull(bundle).getString("FILM_ID");

        // List
        List<Cast> castList = new ArrayList<>();
        List<Crew> crewList = new ArrayList<>();

        // GET
        FilmDetailRequest filmDetailRequest = new FilmDetailRequest(context, castList, crewList, recyclerCast, recyclerCrew, filmID);
        filmDetailRequest.sendRequest(new FilmDetailRequest.APICallback() {
            @Override
            public void onSuccess(FilmDetail filmDetail) {
                // Parsing
                title = filmDetail.getOriginal_title();
                String date = new ParseDate().getDate(filmDetail.getRelease_date());
                String backdrop = new ParseImage().getImage(filmDetail.getBackdrop_path());
                String poster = new ParseImage().getImage(filmDetail.getPoster_path());

                // Toolbar
                toolbar.setTitle(title);

                // Request
                RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).placeholder(R.color.colorPrimaryDark);

                // Isi
                filmTitle.setText(title);
                filmGenre.setText(filmDetail.getGenre());
                filmDate.setText(date);
                filmRuntime.setText(String.format("%s menit", String.valueOf(filmDetail.getRuntime())));
                filmOverview.setText(filmDetail.getOverview());
                Glide.with(context).load(backdrop).apply(requestOptions).into(filmBackdrop);
                Glide.with(context).load(poster).apply(requestOptions).into(filmPoster);

                progressBar.setVisibility(View.GONE);
                scroll.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                emptyResult.setVisibility(View.VISIBLE);
                Snackbar.make(layout, R.string.get_error, Snackbar.LENGTH_LONG).show();
            }
        });

        // Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
