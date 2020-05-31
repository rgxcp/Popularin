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
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.Cast;
import xyz.fairportstudios.popularin.services.ParseImage;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    private Context context;
    private List<Cast> castList;

    public CastAdapter(Context context, List<Cast> castList) {
        this.context = context;
        this.castList = castList;
    }

    private Integer pxToDp(Integer px) {
        float dp = px * context.getResources().getDisplayMetrics().density;
        return (int) dp;
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CastViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_credit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        // ID
        // final String castID = String.valueOf(castList.get(position).getId());

        // Parsing
        String castProfile = new ParseImage().getImage(castList.get(position).getProfile_path());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorSurface)
                .error(R.color.colorSurface);

        // Isi
        holder.textCastName.setText(castList.get(position).getName());
        holder.textCastAs.setText(castList.get(position).getCharacter());
        Glide.with(context).load(castProfile).apply(requestOptions).into(holder.imageCastProfile);

        // Margin
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position == 0) {
            layoutParams.leftMargin = pxToDp(16);
            layoutParams.rightMargin = pxToDp(8);
        } else if (position == getItemCount() - 1) {
            layoutParams.rightMargin = pxToDp(16);
        } else {
            layoutParams.rightMargin = pxToDp(8);
        }
        holder.itemView.setLayoutParams(layoutParams);

        // Activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Belum ada
            }
        });
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    static class CastViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageCastProfile;
        private TextView textCastName;
        private TextView textCastAs;

        CastViewHolder(@NonNull View itemView) {
            super(itemView);

            imageCastProfile = itemView.findViewById(R.id.image_rcr_profile);
            textCastName = itemView.findViewById(R.id.text_rcr_name);
            textCastAs = itemView.findViewById(R.id.text_rcr_as);
        }
    }
}
