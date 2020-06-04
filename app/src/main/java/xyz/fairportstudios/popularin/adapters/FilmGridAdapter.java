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
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.statics.Popularin;

public class FilmGridAdapter extends RecyclerView.Adapter<FilmGridAdapter.FilmGridViewHolder> {
    private Context context;
    private List<Film> filmList;

    public FilmGridAdapter(Context context, List<Film> filmList) {
        this.context = context;
        this.filmList = filmList;
    }

    private Integer pxToDp(Integer px) {
        float dp = px * context.getResources().getDisplayMetrics().density;
        return (int) dp;
    }

    @NonNull
    @Override
    public FilmGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilmGridViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_film_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FilmGridViewHolder holder, int position) {
        // ID
        final String filmID = String.valueOf(filmList.get(position).getId());

        // Parsing
        final String filmTitle = filmList.get(position).getOriginal_title();
        final String filmYear = new ParseDate().getYear(filmList.get(position).getRelease_date());
        final String filmPoster = new ParseImage().getImage(filmList.get(position).getPoster_path());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorSurface)
                .error(R.color.colorSurface);

        // Isi
        Glide.with(context).load(filmPoster).apply(requestOptions).into(holder.imagePoster);

        // Margin
        int left = pxToDp(2);
        int top = pxToDp(2);
        int right = pxToDp(2);
        int bottom = pxToDp(2);

        boolean isEdgeLeft = (position % 4) == 0;
        boolean isEdgeTop = position < 4;
        boolean isEdgeRight = (position % 4) == 3;
        boolean isEdgeBottom = position >= (getItemCount() - 4);

        if (isEdgeLeft) {
            left = pxToDp(4);
        }

        if (isEdgeTop) {
            top = pxToDp(4);
        }

        if (isEdgeRight) {
            right = pxToDp(4);
        }

        if (isEdgeBottom) {
            bottom = pxToDp(4);
        }

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        layoutParams.setMargins(left, top, right, bottom);
        holder.itemView.setLayoutParams(layoutParams);

        // Activity
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

    static class FilmGridViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagePoster;

        FilmGridViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePoster = itemView.findViewById(R.id.image_rfg_poster);
        }
    }
}
