package xyz.fairportstudios.popularin.apis.popularin.get;

import android.content.Context;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.FilmMetadata;
import xyz.fairportstudios.popularin.secrets.APIKey;
import xyz.fairportstudios.popularin.statics.PopularinAPI;

public class FilmMetadataRequest {
    private Context mContext;
    private int mFilmID;

    public FilmMetadataRequest(Context context, int filmID) {
        mContext = context;
        mFilmID = filmID;
    }

    public interface Callback {
        void onSuccess(FilmMetadata filmMetadata);

        void onNotFound();

        void onError(String message);
    }

    public void sendRequest(final Callback callback) {
        String requestURL = PopularinAPI.FILM + mFilmID;

        JsonObjectRequest filmMetadata = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONObject metadataObject = resultObject.getJSONObject("metadata");

                        FilmMetadata filmMetadata = new FilmMetadata(
                                metadataObject.getDouble("average_rating"),
                                metadataObject.getInt("total_review"),
                                metadataObject.getInt("total_favorite"),
                                metadataObject.getInt("total_watchlist")
                        );

                        callback.onSuccess(filmMetadata);
                    } else if (status == 606) {
                        callback.onNotFound();
                    } else {
                        callback.onError(mContext.getString(R.string.general_error));
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                    callback.onError(mContext.getString(R.string.general_error));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof NetworkError || error instanceof TimeoutError) {
                    callback.onError(mContext.getString(R.string.network_error));
                } else if (error instanceof ServerError) {
                    callback.onError(mContext.getString(R.string.server_error));
                } else {
                    callback.onError(mContext.getString(R.string.general_error));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("API-Key", APIKey.POPULARIN_API_KEY);
                return headers;
            }
        };

        Volley.newRequestQueue(mContext).add(filmMetadata);
    }
}
