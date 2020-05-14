package xyz.fairportstudios.popularin.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.Calendar;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.post.AddReviewRequest;
import xyz.fairportstudios.popularin.fragments.DatePickerFragment;

public class AddReviewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Context context;
    private EditText inputReview;
    private Integer year;
    private Integer month;
    private Integer day;
    private RelativeLayout relativeLayout;
    private String pickedDate;
    private String rating;
    private TextView watchDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        // Binding
        context = AddReviewActivity.this;
        inputReview = findViewById(R.id.input_adr_review);
        relativeLayout = findViewById(R.id.layout_adr_anchor);
        watchDate = findViewById(R.id.text_adr_watch_date);
        ImageView filmPoster = findViewById(R.id.image_adr_poster);
        LinearLayout linearLayout = findViewById(R.id.review_adr_layout);
        ProgressBar progressBar = findViewById(R.id.pbr_adr_layout);
        RatingBar ratingBar = findViewById(R.id.rbr_adr_layout);
        TextView filmTitle = findViewById(R.id.text_adr_title);
        TextView filmYear = findViewById(R.id.text_adr_year);
        // TextView networkError = findViewById(R.id.text_adr_empty);
        Toolbar toolbar = findViewById(R.id.toolbar_adr_layout);

        // Extra
        Intent intent = getIntent();
        final String filmID = intent.getStringExtra("FILM_ID");
        String title = intent.getStringExtra("FILM_TITLE");
        String releaseYear = intent.getStringExtra("FILM_YEAR");
        String poster = intent.getStringExtra("FILM_POSTER");

        // Date
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        pickedDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        // SET
        filmTitle.setText(title);
        filmYear.setText(releaseYear);
        watchDate.setText(pickedDate);
        Glide.with(context).load(poster).apply(new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).error(R.color.colorPrimary)).into(filmPoster);
        progressBar.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);

        // Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String reviewText = inputReview.getText().toString();
                String date = year + "-" + (month + 1) + "-" + day;

                AddReviewRequest addReviewRequest = new AddReviewRequest(context, filmID, rating, reviewText, date);
                addReviewRequest.sendRequest(new AddReviewRequest.APICallback() {
                    @Override
                    public void onSuccess() {
                        Snackbar.make(relativeLayout, R.string.review_added, Snackbar.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onBackPressed();
                            }
                        }, 500);
                    }

                    @Override
                    public void onFailed(String message) {
                        Snackbar.make(relativeLayout, message, Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        Snackbar.make(relativeLayout, R.string.add_review_error, Snackbar.LENGTH_LONG).show();
                    }
                });

                return true;
            }
        });

        watchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = String.valueOf(ratingBar.getRating());
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
        Calendar calendar = Calendar.getInstance();
        year = y;
        month = m;
        day = d;
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        pickedDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        watchDate.setText(pickedDate);
    }
}
