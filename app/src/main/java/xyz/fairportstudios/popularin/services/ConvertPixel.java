package xyz.fairportstudios.popularin.services;

import android.content.Context;

public class ConvertPixel {
    private Context context;

    public ConvertPixel(Context context) {
        this.context = context;
    }

    public Integer getDensity(int px) {
        float dp = px * context.getResources().getDisplayMetrics().density;
        return (int) dp;
    }
}
