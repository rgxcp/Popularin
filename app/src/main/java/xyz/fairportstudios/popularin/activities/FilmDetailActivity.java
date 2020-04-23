package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.tmdb.FilmDetail;

public class FilmDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        Context context = FilmDetailActivity.this;
        ImageView backdrop = findViewById(R.id.img_afd_backdrop);
        ImageView poster = findViewById(R.id.img_afd_poster);
        TextView title = findViewById(R.id.txt_afd_title);
        TextView year = findViewById(R.id.txt_afd_year);
        TextView runtime = findViewById(R.id.txt_afd_runtime);
        TextView overview = findViewById(R.id.txt_afd_overview);

        Bundle bundle = getIntent().getExtras();
        String filmID = Objects.requireNonNull(bundle).getString("FILM_ID");

        FilmDetail filmDetail = new FilmDetail(context, backdrop, poster, title, year, runtime, overview);
        String requestURL = filmDetail.getRequestURL(filmID);

        filmDetail.parseJSON(requestURL);
    }
}
