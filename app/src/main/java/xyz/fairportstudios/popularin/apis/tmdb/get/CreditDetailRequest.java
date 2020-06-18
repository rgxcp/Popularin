package xyz.fairportstudios.popularin.apis.tmdb.get;

import android.content.Context;

import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class CreditDetailRequest {
    private Context mContext;

    public CreditDetailRequest(Context mContext) {
        this.mContext = mContext;
    }

    public interface Callback {
        void onSuccess();

        void onError();
    }

    public void sendRequest(final Callback callback) {
        String requestURL = TMDbAPI.CREDIT;
    }
}
