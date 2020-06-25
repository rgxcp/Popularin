package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.tmdb.get.CreditDetailRequest;
import xyz.fairportstudios.popularin.models.CreditDetail;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.services.ParseBio;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class CreditBioFragment extends Fragment {
    // Variable untuk fitur onResume
    private boolean mIsResumeFirstTime = true;

    // Variable untuk fitur load
    private boolean mIsLoadFirstTimeSuccess = false;

    // Variable member
    private Context mContext;
    private ImageView mImageProfile;
    private LinearLayout mBioLayout;
    private ProgressBar mProgressBar;
    private RelativeLayout mAnchorLayout;
    private SwipeRefreshLayout mSwipeRefresh;
    private TextView mTextBio;
    private TextView mTextMessage;

    // Variable constructor
    private int mCreditID;

    public CreditBioFragment(int creditID) {
        mCreditID = creditID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_credit_bio, container, false);

        // Context
        mContext = getActivity();

        // Binding
        mImageProfile = view.findViewById(R.id.image_fcb_profile);
        mBioLayout = view.findViewById(R.id.layout_fcb_bio);
        mProgressBar = view.findViewById(R.id.pbr_fcb_layout);
        mAnchorLayout = view.findViewById(R.id.anchor_fcb_layout);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh_fcb_layout);
        mTextBio = view.findViewById(R.id.text_fcb_bio);
        mTextMessage = view.findViewById(R.id.text_fcb_message);

        // Activity
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mIsLoadFirstTimeSuccess) {
                    mSwipeRefresh.setRefreshing(true);
                    getCreditBio();
                } else {
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsResumeFirstTime) {
            // Mendapatkan data
            mIsResumeFirstTime = false;
            getCreditBio();
        }
    }

    private void getCreditBio() {
        CreditDetailRequest creditDetailRequest = new CreditDetailRequest(mContext, mCreditID);
        creditDetailRequest.sendRequest(new CreditDetailRequest.Callback() {
            @Override
            public void onSuccess(CreditDetail creditDetail, List<Film> filmAsCastList, List<Film> filmAsCrewList) {
                // Parsing
                String bio = new ParseBio().getBioForHumans(creditDetail);
                String profile = TMDbAPI.BASE_SMALL_IMAGE_URL + creditDetail.getProfile_path();

                // Isi
                mTextBio.setText(bio);
                Glide.with(mContext).load(profile).into(mImageProfile);
                mProgressBar.setVisibility(View.GONE);
                mTextMessage.setVisibility(View.GONE);
                mBioLayout.setVisibility(View.VISIBLE);
                mIsLoadFirstTimeSuccess = true;
            }

            @Override
            public void onError(String message) {
                if (!mIsLoadFirstTimeSuccess) {
                    mProgressBar.setVisibility(View.GONE);
                    mTextMessage.setVisibility(View.VISIBLE);
                    mTextMessage.setText(message);
                } else {
                    Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        // Memberhentikan loading
        mSwipeRefresh.setRefreshing(false);
    }
}
