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
import xyz.fairportstudios.popularin.activities.FilmDetailActivity;
import xyz.fairportstudios.popularin.models.FilmList;

public class FilmListAdapter extends RecyclerView.Adapter<FilmListAdapter.FilmListViewHolder> {
    private Context context;
    private List<FilmList> filmLists;

    public FilmListAdapter(Context context, List<FilmList> filmLists) {
        this.context = context;
        this.filmLists = filmLists;
    }

    @NonNull
    @Override
    public FilmListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilmListViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_film_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FilmListViewHolder holder, int position) {
        final int filmID = filmLists.get(position).getId();

        String poster = "https://image.tmdb.org/t/p/w600_and_h900_bestv2" + filmLists.get(position).getPoster_path();

        holder.filmTitle.setText(filmLists.get(position).getOriginal_title());
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimaryDark).error(R.color.colorPrimaryDark);
        Glide.with(context).load(poster).apply(requestOptions).into(holder.filmPoster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmDetail = new Intent(context, FilmDetailActivity.class);
                gotoFilmDetail.putExtra("FILM_ID", String.valueOf(filmID));
                context.startActivity(gotoFilmDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filmLists.size();
    }

    static class FilmListViewHolder extends RecyclerView.ViewHolder {
        ImageView filmPoster;
        TextView filmTitle;

        FilmListViewHolder(@NonNull View itemView) {
            super(itemView);

            filmPoster = itemView.findViewById(R.id.img_rvfl_poster);
            filmTitle = itemView.findViewById(R.id.txt_rvfl_title);
        }
    }
}
