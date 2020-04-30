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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView genreAction = view.findViewById(R.id.image_fh_action);
        ImageView genreAnimation = view.findViewById(R.id.image_fh_animation);
        ImageView genreDrama = view.findViewById(R.id.image_fh_drama);
        ImageView genreFantasy = view.findViewById(R.id.image_fh_fantasy);
        ImageView genreFiction = view.findViewById(R.id.image_fh_fiction);
        ImageView genreHorror = view.findViewById(R.id.image_fh_horror);
        ImageView genreCrime = view.findViewById(R.id.image_fh_crime);
        ImageView genreFamily = view.findViewById(R.id.image_fh_family);
        ImageView genreComedy = view.findViewById(R.id.image_fh_comedy);
        ImageView genreMystery = view.findViewById(R.id.image_fh_mystery);
        ImageView genreWar = view.findViewById(R.id.image_fh_war);
        ImageView genreAdventure = view.findViewById(R.id.image_fh_adventure);
        ImageView genreRomance = view.findViewById(R.id.image_fh_romance);
        ImageView genreHistory = view.findViewById(R.id.image_fh_history);
        ImageView genreThriller = view.findViewById(R.id.image_fh_thriller);

        genreAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmList = new Intent(getActivity(), FilmListActivity.class);
                gotoFilmList.putExtra("GENRE_ID", "28");
                gotoFilmList.putExtra("GENRE_TITLE", "Aksi");
                startActivity(gotoFilmList);
            }
        });

        genreAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmList = new Intent(getActivity(), FilmListActivity.class);
                gotoFilmList.putExtra("GENRE_ID", "16");
                gotoFilmList.putExtra("GENRE_TITLE", "Animasi");
                startActivity(gotoFilmList);
            }
        });

        genreDrama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmList = new Intent(getActivity(), FilmListActivity.class);
                gotoFilmList.putExtra("GENRE_ID", "18");
                gotoFilmList.putExtra("GENRE_TITLE", "Drama");
                startActivity(gotoFilmList);
            }
        });

        genreFantasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmList = new Intent(getActivity(), FilmListActivity.class);
                gotoFilmList.putExtra("GENRE_ID", "14");
                gotoFilmList.putExtra("GENRE_TITLE", "Fantasi");
                startActivity(gotoFilmList);
            }
        });

        genreFiction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmList = new Intent(getActivity(), FilmListActivity.class);
                gotoFilmList.putExtra("GENRE_ID", "878");
                gotoFilmList.putExtra("GENRE_TITLE", "Fiksi");
                startActivity(gotoFilmList);
            }
        });

        genreHorror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmList = new Intent(getActivity(), FilmListActivity.class);
                gotoFilmList.putExtra("GENRE_ID", "27");
                gotoFilmList.putExtra("GENRE_TITLE", "Horor");
                startActivity(gotoFilmList);
            }
        });

        genreCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmList = new Intent(getActivity(), FilmListActivity.class);
                gotoFilmList.putExtra("GENRE_ID", "80");
                gotoFilmList.putExtra("GENRE_TITLE", "Kejahatan");
                startActivity(gotoFilmList);
            }
        });

        genreFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmList = new Intent(getActivity(), FilmListActivity.class);
                gotoFilmList.putExtra("GENRE_ID", "10751");
                gotoFilmList.putExtra("GENRE_TITLE", "Keluarga");
                startActivity(gotoFilmList);
            }
        });

        genreComedy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmList = new Intent(getActivity(), FilmListActivity.class);
                gotoFilmList.putExtra("GENRE_ID", "35");
                gotoFilmList.putExtra("GENRE_TITLE", "Komedi");
                startActivity(gotoFilmList);
            }
        });

        genreMystery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmList = new Intent(getActivity(), FilmListActivity.class);
                gotoFilmList.putExtra("GENRE_ID", "9648");
                gotoFilmList.putExtra("GENRE_TITLE", "Misteri");
                startActivity(gotoFilmList);
            }
        });

        genreWar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmList = new Intent(getActivity(), FilmListActivity.class);
                gotoFilmList.putExtra("GENRE_ID", "10752");
                gotoFilmList.putExtra("GENRE_TITLE", "Perang");
                startActivity(gotoFilmList);
            }
        });

        genreAdventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmList = new Intent(getActivity(), FilmListActivity.class);
                gotoFilmList.putExtra("GENRE_ID", "12");
                gotoFilmList.putExtra("GENRE_TITLE", "Petualang");
                startActivity(gotoFilmList);
            }
        });

        genreRomance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmList = new Intent(getActivity(), FilmListActivity.class);
                gotoFilmList.putExtra("GENRE_ID", "10749");
                gotoFilmList.putExtra("GENRE_TITLE", "Romansa");
                startActivity(gotoFilmList);
            }
        });

        genreHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmList = new Intent(getActivity(), FilmListActivity.class);
                gotoFilmList.putExtra("GENRE_ID", "36");
                gotoFilmList.putExtra("GENRE_TITLE", "Sejarah");
                startActivity(gotoFilmList);
            }
        });

        genreThriller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmList = new Intent(getActivity(), FilmListActivity.class);
                gotoFilmList.putExtra("GENRE_ID", "53");
                gotoFilmList.putExtra("GENRE_TITLE", "Thriller");
                startActivity(gotoFilmList);
            }
        });

        return view;
    }
}
