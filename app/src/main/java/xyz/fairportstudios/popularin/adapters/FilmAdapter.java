package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseGenre;
import xyz.fairportstudios.popularin.services.ParseImage;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {
    private Context context;
    private List<Film> filmList;

    public FilmAdapter(Context context, List<Film> filmList) {
        this.context = context;
        this.filmList = filmList;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilmViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_film, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        // Film ID
        final String filmID = String.valueOf(filmList.get(position).getId());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).error(R.color.colorPrimary);

        // Parsing
        String date = new ParseDate().getDate(filmList.get(position).getRelease_date());
        String genre = new ParseGenre().getGenre(filmList.get(position).getGenre_ids());
        String poster = new ParseImage().getPoster(filmList.get(position).getPoster_path());

        // Mengisi data
        holder.filmTitle.setText(filmList.get(position).getOriginal_title());
        holder.filmGenre.setText(genre);
        holder.filmReleaseDate.setText(date);
        Glide.with(context).load(poster).apply(requestOptions).into(holder.filmPoster);

        // Activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent gotoFilmDetail = new Intent(context, FilmDetailActivity.class);
                gotoFilmDetail.putExtra("FILM_ID", filmID);
                context.startActivity(gotoFilmDetail);
                 */
            }
        });
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    static class FilmViewHolder extends RecyclerView.ViewHolder {
        ImageView filmPoster;
        TextView filmTitle;
        TextView filmGenre;
        TextView filmReleaseDate;

        FilmViewHolder(@NonNull View itemView) {
            super(itemView);

            filmPoster = itemView.findViewById(R.id.image_rvf_poster);
            filmTitle = itemView.findViewById(R.id.text_rvf_title);
            filmGenre = itemView.findViewById(R.id.text_rvf_genre);
            filmReleaseDate = itemView.findViewById(R.id.text_rvf_release_date);
        }
    }
}