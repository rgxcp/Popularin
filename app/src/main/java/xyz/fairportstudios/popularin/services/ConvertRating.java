package xyz.fairportstudios.popularin.services;

import java.util.HashMap;
import java.util.Map;

import xyz.fairportstudios.popularin.R;

public class ConvertRating {
    public Integer getStar(double rating) {
        Map<Double, Integer> stars = new HashMap<>();
        stars.put(0.5, R.drawable.ic_star_05);
        stars.put(1.0, R.drawable.ic_star_10);
        stars.put(1.5, R.drawable.ic_star_15);
        stars.put(2.0, R.drawable.ic_star_20);
        stars.put(2.5, R.drawable.ic_star_25);
        stars.put(3.0, R.drawable.ic_star_30);
        stars.put(3.5, R.drawable.ic_star_35);
        stars.put(4.0, R.drawable.ic_star_40);
        stars.put(4.5, R.drawable.ic_star_45);
        stars.put(5.0, R.drawable.ic_star_50);

        return stars.get(rating);
    }
}
