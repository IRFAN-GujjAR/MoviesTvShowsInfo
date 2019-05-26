package com.example.moviestvshowsinfo.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviestvshowsinfo.AsynTaskLoaderJSONPasrsing.CheckInternetConnectionAndResultsLoader;
import com.example.moviestvshowsinfo.R;
import com.example.moviestvshowsinfo.Utils.Utils;


public class EmptyFragment extends Fragment implements LoaderManager.LoaderCallbacks<Boolean[]> {

    private View view = null;

    private TextView emptyTextView;
    private ImageView retryImageView;


    public EmptyFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.empty_fragment_layout, container, false);
        view.setVisibility(View.GONE);

        emptyTextView = (TextView) view.findViewById(R.id.empty_text_view);
        retryImageView = (ImageView) view.findViewById(R.id.retry_image_view);
        retryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setEmptyMovieTvShowList(false);
                Utils.screenSlidePagerAdpater.notifyDataChanged();
            }
        });

        if(!Utils.isFavouriteMovies()&&!Utils.isFavouriteTvShows()){
            /**
             * Initializing the loader which will check both internet connection and the results found or not.
             */
            getLoaderManager().initLoader(1, null, this);

        }else {
            if(Utils.isFavouriteMovies()) {
                emptyTextView.setText("No Favourites Movies Available!");
            }else if(Utils.isFavouriteTvShows()){
                emptyTextView.setText("No Favourite TV Shows Available!");
            }
            retryImageView.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
        }

        return view;
    }



    @NonNull
    @Override
    public Loader<Boolean[]> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CheckInternetConnectionAndResultsLoader(getContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Boolean[]> loader, Boolean[] booleans) {
        if (booleans[0] == false && booleans[1] == false) {
            emptyTextView.setText("Check Your Internet Connection!");
            retryImageView.setVisibility(View.VISIBLE);

        } else if (booleans[0] == true && booleans[1] == true) {
            emptyTextView.setText("No Results Found!");
            retryImageView.setVisibility(View.GONE);
        }

        view.setVisibility(View.VISIBLE);
        getLoaderManager().destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Boolean[]> loader) {

    }
}
