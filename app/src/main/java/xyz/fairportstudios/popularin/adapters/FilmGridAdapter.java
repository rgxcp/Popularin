package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import xyz.fairportstudios.popularin.services.ConvertPixel;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.statics.Popularin;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class FilmGridAdapter extends RecyclerView.Adapter<FilmGridAdapter.FilmGridViewHolder> {
    private Context context;
    private List<Film> filmList;

    public FilmGridAdapter(Context context, List<Film> filmList) {
        this.context = context;
        this.filmList = filmList;
    }

    @NonNull
    @Override
    public FilmGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilmGridViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_film_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FilmGridViewHolder holder, int position) {
        // Posisi
        Film currentItem = filmList.get(position);

        // Extra
        final Integer filmID = currentItem.getId();

        // Parsing
        final String filmTitle = currentItem.getOriginal_title();
        final String filmYear = new ParseDate().getYear(currentItem.getRelease_date());
        final String filmPoster = TMDbAPI.BASE_SMALL_IMAGE_URL + currentItem.getPoster_path();

        // Request gambar
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorSurface)
                .error(R.color.colorSurface);

        // Isi
        Glide.with(context).load(filmPoster).apply(requestOptions).into(holder.imageFilmPoster);

        // Margin
        ConvertPixel convertPixel = new ConvertPixel(context);

        int left = convertPixel.getDensity(4);
        int top = convertPixel.getDensity(4);
        int right = convertPixel.getDensity(4);
        int bottom = convertPixel.getDensity(4);

        boolean isEdgeLeft = (position % 4) == 0;
        boolean isEdgeTop = position < 4;
        boolean isEdgeRight = (position % 4) == 3;
        boolean isEdgeBottom = position >= (getItemCount() - 4);

        if (isEdgeLeft) {
            left = convertPixel.getDensity(8);
        }
        if (isEdgeTop) {
            top = convertPixel.getDensity(8);
        }
        if (isEdgeRight) {
            right = convertPixel.getDensity(8);
        }
        if (isEdgeBottom) {
            bottom = convertPixel.getDensity(8);
        }

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        layoutParams.setMargins(left, top, right, bottom);
        holder.itemView.setLayoutParams(layoutParams);

        // Activity
        holder.imageFilmPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FilmDetailActivity.class);
                intent.putExtra(Popularin.FILM_ID, filmID);
                context.startActivity(intent);
            }
        });

        holder.imageFilmPoster.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FilmModal filmModal = new FilmModal(filmID, filmTitle, filmYear, filmPoster);
                filmModal.show(fragmentManager, Popularin.FILM_MODAL);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    static class FilmGridViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageFilmPoster;

        FilmGridViewHolder(@NonNull View itemView) {
            super(itemView);

            imageFilmPoster = itemView.findViewById(R.id.image_rfg_poster);
        }
    }
}
