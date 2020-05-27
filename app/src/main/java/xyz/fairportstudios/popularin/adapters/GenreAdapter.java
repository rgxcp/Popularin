package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.FilmListActivity;
import xyz.fairportstudios.popularin.models.Genre;
import xyz.fairportstudios.popularin.services.Popularin;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    private Context context;
    private List<Genre> genreList;

    public GenreAdapter(Context context, List<Genre> genreList) {
        this.context = context;
        this.genreList = genreList;
    }

    private Integer pxToDp(Integer px) {
        float dp = px * context.getResources().getDisplayMetrics().density;
        return (int) dp;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GenreViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_genre, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        // Extra
        final String genreID = String.valueOf(genreList.get(position).getId());
        final String genreTitle = genreList.get(position).getTitle();

        // Isi
        holder.textGenreTitle.setText(genreTitle);

        // Margin
        int left = pxToDp(4);
        int top = pxToDp(4);
        int right = pxToDp(4);
        int bottom = pxToDp(4);

        boolean isEdgeLeft = (position % 2) == 0;
        boolean isEdgeTop = position < 2;
        boolean isEdgeRight = (position % 2) == 1;
        boolean isEdgeBottom = position >= (getItemCount() - 2);

        if (isEdgeLeft) {
            left = pxToDp(16);
        }

        if (isEdgeTop) {
            top = pxToDp(16);
        }

        if (isEdgeRight) {
            right = pxToDp(16);
        }

        if (isEdgeBottom) {
            bottom = pxToDp(16);
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

        GenreViewHolder(@NonNull View itemView) {
            super(itemView);

            textGenreTitle = itemView.findViewById(R.id.text_rg_title);
        }
    }
}
