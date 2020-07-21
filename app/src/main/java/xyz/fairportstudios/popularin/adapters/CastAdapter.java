package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.Cast;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    private Context mContext;
    private List<Cast> mCastList;
    private OnClickListener mOnClickListener;

    public CastAdapter(Context context, List<Cast> castList, OnClickListener onClickListener) {
        mContext = context;
        mCastList = castList;
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onCastItemClick(int position);
    }

    private int getDensity(int px) {
        float dp = px * mContext.getResources().getDisplayMetrics().density;
        return (int) dp;
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CastViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_credit, parent, false), mOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        // Posisi
        Cast currentItem = mCastList.get(position);

        // Parsing
        String profile = TMDbAPI.BASE_SMALL_IMAGE_URL + currentItem.getProfile_path();

        // Isi
        holder.mTextName.setText(currentItem.getName());
        holder.mTextAs.setText(currentItem.getCharacter());
        Glide.with(mContext).load(profile).into(holder.mImageProfile);

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
        return mCastList.size();
    }

    static class CastViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageProfile;
        private TextView mTextName;
        private TextView mTextAs;
        private OnClickListener mOnClickListener;

        CastViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);

            mImageProfile = itemView.findViewById(R.id.image_rcr_profile);
            mTextName = itemView.findViewById(R.id.text_rcr_name);
            mTextAs = itemView.findViewById(R.id.text_rcr_as);
            mOnClickListener = onClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                mOnClickListener.onCastItemClick(getAdapterPosition());
            }
        }
    }
}
