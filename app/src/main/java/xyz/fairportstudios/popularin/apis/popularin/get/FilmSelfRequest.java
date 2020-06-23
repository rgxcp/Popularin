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
import xyz.fairportstudios.popularin.secrets.APIKey;
import xyz.fairportstudios.popularin.statics.PopularinAPI;
import xyz.fairportstudios.popularin.models.FilmSelf;
import xyz.fairportstudios.popularin.preferences.Auth;

public class FilmSelfRequest {
    private Context mContext;
    private int mFilmID;

    public FilmSelfRequest(Context context, int filmID) {
        mContext = context;
        mFilmID = filmID;
    }

    public interface Callback {
        void onSuccess(FilmSelf filmSelf);

        void onError(String message);
    }

    public void sendRequest(final Callback callback) {
        String requestURL = PopularinAPI.FILM + mFilmID + "/self";

        JsonObjectRequest filmSelf = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        JSONObject resultObject = response.getJSONObject("result");

                        FilmSelf filmSelf = new FilmSelf(
                                resultObject.getBoolean("in_review"),
                                resultObject.getBoolean("in_favorite"),
                                resultObject.getBoolean("in_watchlist"),
                                resultObject.getDouble("last_rate")
                        );

                        callback.onSuccess(filmSelf);
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
                headers.put("Auth-Token", new Auth(mContext).getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(mContext).add(filmSelf);
    }
}
