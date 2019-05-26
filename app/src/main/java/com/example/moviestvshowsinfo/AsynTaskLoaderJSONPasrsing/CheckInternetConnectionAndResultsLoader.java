package com.example.moviestvshowsinfo.AsynTaskLoaderJSONPasrsing;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.moviestvshowsinfo.Utils.JSONUtils.JSONParsingForCheckingInternetAndResults;
import com.example.moviestvshowsinfo.Utils.Utils;

/**
 * This loader checks both internet connection and results found or not.
 */
public class CheckInternetConnectionAndResultsLoader extends AsyncTaskLoader<Boolean[]> {


    public CheckInternetConnectionAndResultsLoader(Context context){
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public Boolean[] loadInBackground() {
        return JSONParsingForCheckingInternetAndResults.getConnectionAndResults(Utils.getCurrentUrl());
    }
}
