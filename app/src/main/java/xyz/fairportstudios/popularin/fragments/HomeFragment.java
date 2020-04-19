package xyz.fairportstudios.popularin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.FilmListActivity;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView mAction = mView.findViewById(R.id.ctg_action);
        ImageView mDrama = mView.findViewById(R.id.ctg_drama);
        ImageView mFantasy = mView.findViewById(R.id.ctg_fantasy);

        mAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mGotoFilmList = new Intent(getActivity(), FilmListActivity.class);
                mGotoFilmList.putExtra("GENRE_ID", "28");
                mGotoFilmList.putExtra("GENRE_TITLE", "Aksi");
                startActivity(mGotoFilmList);
            }
        });

        mDrama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mGotoFilmList = new Intent(getActivity(), FilmListActivity.class);
                mGotoFilmList.putExtra("GENRE_ID", "18");
                mGotoFilmList.putExtra("GENRE_TITLE", "Drama");
                startActivity(mGotoFilmList);
            }
        });

        mFantasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mGotoFilmList = new Intent(getActivity(), FilmListActivity.class);
                mGotoFilmList.putExtra("GENRE_ID", "14");
                mGotoFilmList.putExtra("GENRE_TITLE", "Fantasi");
                startActivity(mGotoFilmList);
            }
        });

        return mView;
    }
}
