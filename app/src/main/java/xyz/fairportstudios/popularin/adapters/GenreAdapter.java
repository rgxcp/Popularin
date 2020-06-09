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

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.FilmListActivity;
import xyz.fairportstudios.popularin.models.Genre;
import xyz.fairportstudios.popularin.services.ConvertPixel;
import xyz.fairportstudios.popularin.statics.Popularin;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    private Context context;
    private List<Genre> genreList;

    public GenreAdapter(Context context, List<Genre> genreList) {
        this.context = context;
        this.genreList = genreList;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GenreViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_genre, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        // Posisi
        Genre currentItem = genreList.get(position);

        // Extra
        final Integer genreID = currentItem.getId();
        final String genreTitle = currentItem.getTitle();

        // Isi
        holder.textGenreTitle.setText(genreTitle);
        holder.imageGenreBackground.setImageResource(currentItem.getBackground());

        // Margin
        ConvertPixel convertPixel = new ConvertPixel(context);

        int left = convertPixel.getDensity(4);
        int top = convertPixel.getDensity(4);
        int right = convertPixel.getDensity(4);
        int bottom = convertPixel.getDensity(4);

        boolean isEdgeLeft = (position % 2) == 0;
        boolean isEdgeTop = position < 2;
        boolean isEdgeRight = (position % 2) == 1;
        boolean isEdgeBottom = position >= (getItemCount() - 2);

        if (isEdgeLeft) {
            left = convertPixel.getDensity(16);
        }
        if (isEdgeTop) {
            top = convertPixel.getDensity(16);
        }
        if (isEdgeRight) {
            right = convertPixel.getDensity(16);
        }
        if (isEdgeBottom) {
            bottom = convertPixel.getDensity(16);
        }

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        layoutParams.setMargins(left, top, right, bottom);
        holder.itemView.setLayoutParams(layoutParams);

        // Activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FilmListActivity.class);
                intent.putExtra(Popularin.GENRE_ID, genreID);
                intent.putExtra(Popularin.GENRE_TITLE, genreTitle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    static class GenreViewHolder extends RecyclerView.ViewHolder {
        private TextView textGenreTitle;
        private ImageView imageGenreBackground;

        GenreViewHolder(@NonNull View itemView) {
            super(itemView);

            textGenreTitle = itemView.findViewById(R.id.text_rg_title);
            imageGenreBackground = itemView.findViewById(R.id.image_rg_background);
        }
    }
}
