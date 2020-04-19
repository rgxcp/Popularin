package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.util.Log;
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
import xyz.fairportstudios.popularin.models.FilmList;

public class FilmListAdapter extends RecyclerView.Adapter<FilmListAdapter.FilmListViewHolder> {
    private Context mContext;
    private List<FilmList> mFilmList;

    public FilmListAdapter(Context mContext, List<FilmList> mFilmList) {
        this.mContext = mContext;
        this.mFilmList = mFilmList;
    }

    @NonNull
    @Override
    public FilmListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilmListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_view_film_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FilmListViewHolder holder, int position) {
        final int mFilmID = mFilmList.get(position).getId();

        String mPoster = "https://image.tmdb.org/t/p/w600_and_h900_bestv2" + mFilmList.get(position).getPoster_path();

        holder.mFilmTitle.setText(mFilmList.get(position).getOriginal_title());
        RequestOptions mRequestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimaryDark).error(R.color.colorPrimaryDark);
        Glide.with(mContext).load(mPoster).apply(mRequestOptions).into(holder.mFilmPoster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Film ID", String.valueOf(mFilmID));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilmList.size();
    }

    static class FilmListViewHolder extends RecyclerView.ViewHolder {
        ImageView mFilmPoster;
        TextView mFilmTitle;

        FilmListViewHolder(@NonNull View itemView) {
            super(itemView);

            mFilmPoster = itemView.findViewById(R.id.img_rvfl_poster);
            mFilmTitle = itemView.findViewById(R.id.txt_rvfl_title);
        }
    }
}
