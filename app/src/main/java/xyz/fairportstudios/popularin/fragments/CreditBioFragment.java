package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.CreditDetail;
import xyz.fairportstudios.popularin.services.ParseBio;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class CreditBioFragment extends Fragment {
    // Variable member
    ImageView mImageProfile;
    TextView mTextBio;

    // Variable constructor
    private CreditDetail creditDetail;

    public CreditBioFragment(CreditDetail creditDetail) {
        this.creditDetail = creditDetail;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_credit_bio, container, false);

        // Context
        Context context = getActivity();

        // Binding
        mImageProfile = view.findViewById(R.id.image_fcb_profile);
        mTextBio = view.findViewById(R.id.text_fcb_bio);

        // Mendapatkan data
        getCreditBio(context);

        return view;
    }

    private void getCreditBio(Context context) {
        mTextBio.setText(new ParseBio().getBioForHumans(creditDetail));
        Glide.with(context).load(TMDbAPI.BASE_SMALL_IMAGE_URL + creditDetail.getProfile_path()).into(mImageProfile);
    }
}