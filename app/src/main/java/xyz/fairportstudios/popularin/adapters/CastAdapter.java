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
import xyz.fairportstudios.popularin.activities.CreditDetailActivity;
import xyz.fairportstudios.popularin.models.Cast;
import xyz.fairportstudios.popularin.services.ConvertPixel;
import xyz.fairportstudios.popularin.statics.Popularin;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    private Context context;
    private List<Cast> castList;

    public CastAdapter(Context context, List<Cast> castList) {
        this.context = context;
        this.castList = castList;
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CastViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_credit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        // Posisi
        Cast currentItem = castList.get(position);

        // Extra
        final Integer castID = currentItem.getId();

        // Parsing
        String castProfile = TMDbAPI.BASE_SMALL_IMAGE_URL + currentItem.getProfile_path();

        // Request gambar
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorSurface)
                .error(R.color.colorSurface);

        // Isi
        holder.textCastName.setText(currentItem.getName());
        holder.textCastAs.setText(currentItem.getCharacter());
        Glide.with(context).load(castProfile).apply(requestOptions).into(holder.imageCastProfile);

        // Margin
        ConvertPixel convertPixel = new ConvertPixel(context);

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position == 0) {
            layoutParams.leftMargin = convertPixel.getDensity(16);
            layoutParams.rightMargin = convertPixel.getDensity(8);
        } else if (position == getItemCount() - 1) {
            layoutParams.rightMargin = convertPixel.getDensity(16);
        } else {
            layoutParams.rightMargin = convertPixel.getDensity(8);
        }
        holder.itemView.setLayoutParams(layoutParams);

        // Activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CreditDetailActivity.class);
                intent.putExtra(Popularin.CREDIT_ID, castID);
                intent.putExtra(Popularin.VIEW_PAGER_INDEX, 1);
                context.startActivity(intent);
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
