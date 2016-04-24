package apps.gen.genshelf.controllers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import apps.gen.genshelf.R;
import apps.gen.lib.controllers.Controller;
import apps.gen.lib.utils.H;
import cz.msebera.android.httpclient.Header;

public class LibraryController extends Controller {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.controller_shelf, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setTitle("GenShelf");

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://lofi.e-hentai.org/", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                H.i(String.valueOf(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                H.i(String.format("Error %s", error.toString()));
            }
        });
    }
}
