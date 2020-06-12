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
import xyz.fairportstudios.popularin.models.Crew;
import xyz.fairportstudios.popularin.services.ConvertPixel;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.statics.Popularin;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.CrewViewHolder> {
    private Context context;
    private List<Crew> crewList;

    public CrewAdapter(Context context, List<Crew> crewList) {
        this.context = context;
        this.crewList = crewList;
    }

    @NonNull
    @Override
    public CrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CrewViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_credit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CrewViewHolder holder, int position) {
        // Posisi
        Crew currentItem = crewList.get(position);

        // Extra
        final Integer crewID = currentItem.getId();

        // Parsing
        String crewProfile = new ParseImage().getImage(currentItem.getProfile_path());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorSurface)
                .error(R.color.colorSurface);

        // Isi
        holder.textCrewName.setText(currentItem.getName());
        holder.textCrewAs.setText(currentItem.getJob());
        Glide.with(context).load(crewProfile).apply(requestOptions).into(holder.imageCrewProfile);

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
                intent.putExtra(Popularin.CREDIT_ID, crewID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return crewList.size();
    }

    static class CrewViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageCrewProfile;
        private TextView textCrewName;
        private TextView textCrewAs;

        CrewViewHolder(@NonNull View itemView) {
            super(itemView);

            imageCrewProfile = itemView.findViewById(R.id.image_rcr_profile);
            textCrewName = itemView.findViewById(R.id.text_rcr_name);
            textCrewAs = itemView.findViewById(R.id.text_rcr_as);
        }
    }
}
