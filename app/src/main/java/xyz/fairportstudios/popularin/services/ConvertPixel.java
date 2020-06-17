package xyz.fairportstudios.popularin.services;

import android.content.Context;

public class ConvertPixel {
    private Context mContext;

    public ConvertPixel(Context mContext) {
        this.mContext = mContext;
    }

    public int getDensity(int px) {
        float dp = px * mContext.getResources().getDisplayMetrics().density;
        return (int) dp;
    }
}
