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
import xyz.fairportstudios.popularin.apis.tmdb.SearchFilm;

public class SearchFragment extends Fragment {
    private EditText searchText;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_search, container, false);

        searchText = mView.findViewById(R.id.edt_fs);
        recyclerView = mView.findViewById(R.id.rcv_fs_film);
        ImageButton searchButton = mView.findViewById(R.id.btn_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchText.getText().toString();

                List<FilmList> filmLists = new ArrayList<>();
                SearchFilm searchFilm = new SearchFilm(getActivity(), filmLists, recyclerView);
                String requestURL = searchFilm.getRequestURL(query, 1);
                searchFilm.parseJSON(requestURL);
            }
        });

        return mView;
    }
}
