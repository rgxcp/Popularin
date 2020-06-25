package xyz.fairportstudios.popularin.services;

import java.util.HashMap;
import java.util.Map;

public class ConvertGenre {
    public String getGenreForHumans(int id) {
        Map<Integer, String> genres = new HashMap<>();
        genres.put(0, "Tanpa Genre");
        genres.put(12, "Petualangan");
        genres.put(14, "Fantasi");
        genres.put(16, "Animasi");
        genres.put(18, "Drama");
        genres.put(27, "Horor");
        genres.put(28, "Aksi");
        genres.put(35, "Komedi");
        genres.put(36, "Sejarah");
        genres.put(37, "Barat");
        genres.put(53, "Thriller");
        genres.put(80, "Kejahatan");
        genres.put(99, "Dokumenter");
        genres.put(878, "Fiksi");
        genres.put(9648, "Misteri");
        genres.put(10402, "Musik");
        genres.put(10749, "Romansa");
        genres.put(10751, "Keluarga");
        genres.put(10752, "Perang");
        genres.put(10770, "Serial TV");

        return genres.get(id);
    }
}
