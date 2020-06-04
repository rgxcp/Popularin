package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.FilmDetailActivity;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseGenre;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.statics.Popularin;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {
    private Context context;
    private List<Film> filmList;

    public FilmAdapter(Context context, List<Film> filmList) {
        this.context = context;
        this.filmList = filmList;
    }

    private Integer pxToDp() {
        float dp = 16 * context.getResources().getDisplayMetrics().density;
        return (int) dp;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilmViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_film, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        // ID
        final String filmID = String.valueOf(filmList.get(position).getId());

        // Parsing
        final String filmTitle = filmList.get(position).getOriginal_title();
        final String filmYear = new ParseDate().getYear(filmList.get(position).getRelease_date());
        final String filmPoster = new ParseImage().getImage(filmList.get(position).getPoster_path());
        final String genre = new ParseGenre().getGenre(filmList.get(position).getGenre_id());
        final String releaseDate = new ParseDate().getDate(filmList.get(position).getRelease_date());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorSurface)
                .error(R.color.colorSurface);

        // Isi
        holder.textTitle.setText(filmTitle);
        holder.textGenre.setText(genre);
        holder.textReleaseDate.setText(releaseDate);
        Glide.with(context).load(filmPoster).apply(requestOptions).into(holder.imagePoster);

        // Margin
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position == filmList.size() - 1) {
            layoutParams.bottomMargin = pxToDp();
        }
        holder.itemView.setLayoutParams(layoutParams);

        // Activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FilmDetailActivity.class);
                intent.putExtra(Popularin.FILM_ID, filmID);
                context.startActivity(intent);
            }
        });

        holder.imagePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FilmDetailActivity.class);
                intent.putExtra(Popularin.FILM_ID, filmID);
                context.startActivity(intent);
            }
        });

        holder.imagePoster.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FilmModal filmModal = new FilmModal(filmID, filmTitle, filmYear, filmPoster);
                filmModal.show(fragmentManager, Popularin.FILM_STATUS_MODAL);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    static class FilmViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagePoster;
        private TextView textTitle;
        private TextView textGenre;
        private TextView textReleaseDate;

        FilmViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePoster = itemView.findViewById(R.id.image_rf_poster);
            textTitle = itemView.findViewById(R.id.text_rf_title);
            textGenre = itemView.findViewById(R.id.text_rf_genre);
            textReleaseDate = itemView.findViewById(R.id.text_rf_release_date);
        }
    }
}
