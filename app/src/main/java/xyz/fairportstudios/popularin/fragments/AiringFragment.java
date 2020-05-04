package xyz.fairportstudios.popularin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.tmdb.get.AiringFilm;
import xyz.fairportstudios.popularin.models.Film;

public class AiringFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_airing, container, false);

        // Binding
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_fa_layout);

        // Set-up list
        List<Film> filmList = new ArrayList<>();

        // Mendapatkan data
        AiringFilm airingFilm = new AiringFilm(getActivity(), filmList, recyclerView);
        airingFilm.sendRequest();

        return view;
    }
}
