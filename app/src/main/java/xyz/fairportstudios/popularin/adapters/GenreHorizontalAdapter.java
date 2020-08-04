package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.Genre;

public class GenreHorizontalAdapter extends RecyclerView.Adapter<GenreHorizontalAdapter.GenreHorizontalViewHolder> {
    private Context mContext;
    private List<Genre> mGenreList;
    private OnClickListener mOnClickListener;

    public GenreHorizontalAdapter(Context context, List<Genre> genreList, OnClickListener onClickListener) {
        mContext = context;
        mGenreList = genreList;
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onGenreItemClick(int position);
    }

    private int getDensity(int px) {
        float dp = px * mContext.getResources().getDisplayMetrics().density;
        return (int) dp;
    }

    @NonNull
    @Override
    public GenreHorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GenreHorizontalViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_genre_horizontal, parent, false), mOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreHorizontalViewHolder holder, int position) {
        // Posisi
        Genre currentItem = mGenreList.get(position);

        // Isi
        holder.mTextTitle.setText(currentItem.getTitle());
        holder.mImageBackground.setImageResource(currentItem.getBackground());

        // Margin
        int left = getDensity(6);
        int right = getDensity(6);

        boolean isEdgeLeft = position == 0;
        boolean isEdgeRight = position == getItemCount() - 1;

        if (isEdgeLeft) {
            left = getDensity(16);
        }
        if (isEdgeRight) {
            right = getDensity(16);
        }

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        layoutParams.setMarginStart(left);
        layoutParams.setMarginEnd(right);
        holder.itemView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return mGenreList.size();
    }

    static class GenreHorizontalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageBackground;
        private TextView mTextTitle;
        private OnClickListener mOnClickListener;

        GenreHorizontalViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);

            mImageBackground = itemView.findViewById(R.id.image_rgh_background);
            mTextTitle = itemView.findViewById(R.id.text_rgh_title);
            mOnClickListener = onClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                mOnClickListener.onGenreItemClick(getAdapterPosition());
            }
        }
    }
}
