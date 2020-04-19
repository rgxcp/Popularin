package xyz.fairportstudios.popularin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.FilmList;
import xyz.fairportstudios.popularin.apis.SearchFilm;
import xyz.fairportstudios.popularin.apis.TMDBRequestURL;

public class SearchFragment extends Fragment {
    private EditText mSearchText;
    private List<FilmList> mFilmList;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_search, container, false);

        mFilmList = new ArrayList<>();
        mSearchText = mView.findViewById(R.id.edt_fs);
        mRecyclerView = mView.findViewById(R.id.rcv_fs_film);
        ImageButton mSearchButton = mView.findViewById(R.id.btn_search);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mQuery = mSearchText.getText().toString();

                TMDBRequestURL mTMDBRequestURL = new TMDBRequestURL();
                String mRequestURL = mTMDBRequestURL.getSearchURL(mQuery);

                SearchFilm mSearchFilm = new SearchFilm(mFilmList, mRecyclerView);
                mSearchFilm.parseJSON(mRequestURL, getActivity());
            }
        });

        return mView;
    }
}
