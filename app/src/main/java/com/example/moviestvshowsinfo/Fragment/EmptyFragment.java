package com.example.moviestvshowsinfo.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moviestvshowsinfo.AsynTaskLoaderJSONPasrsing.CheckInternetConnectionAndResultsLoader;
import com.example.moviestvshowsinfo.R;
import com.example.moviestvshowsinfo.Utils.Utils;

public class EmptyFragment extends Fragment implements LoaderManager.LoaderCallbacks<Boolean[]> {


    private LinearLayout linearLayout;
    private ImageView resultsNotFoundImageView;
    private TextView emptyTextView;
    private ImageView retryImageView;
    private TextView resultNotFoundTextView;

    public EmptyFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.empty_fragment_layout, container, false);

        linearLayout=(LinearLayout) view.findViewById(R.id.linear_layout_empty_fragment);
        resultsNotFoundImageView=(ImageView) view.findViewById(R.id.result_not_found_image_view);
        emptyTextView=(TextView) view.findViewById(R.id.empty_text_view);
        retryImageView=(ImageView) view.findViewById(R.id.retry_image_view);
        resultNotFoundTextView=(TextView) view.findViewById(R.id.result_not_found_text_view);

        emptyTextView.setVisibility(View.GONE);
        resultsNotFoundImageView.setVisibility(View.INVISIBLE);
        emptyTextView.setVisibility(View.GONE);
        retryImageView.setVisibility(View.GONE);
        resultNotFoundTextView.setVisibility(View.GONE);

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
            resultsNotFoundImageView.setVisibility(View.VISIBLE);
            resultsNotFoundImageView.setImageResource(R.drawable.ic_star_2);
            emptyTextView.setVisibility(View.VISIBLE);
            resultNotFoundTextView.setVisibility(View.VISIBLE);
            emptyTextView.setTextSize(30);
            if (Utils.isFavouriteMovies()) {
                emptyTextView.setText("No Favourites Movies Available!");
                resultNotFoundTextView.setText("Add some Movies to favourites, so you can watch those later when you are offline.");
            } else if (Utils.isFavouriteTvShows()) {
                emptyTextView.setText("No Favourite TV Shows Available!");
                resultNotFoundTextView.setText("Add some TV Shows to favourites, so you can watch those later when you are offline.");
            }

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
            resultsNotFoundImageView.setVisibility(View.VISIBLE);
            resultsNotFoundImageView.setImageResource(R.drawable.ic_worldwide);
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText("Check Your Internet Connection!");
            retryImageView.setVisibility(View.VISIBLE);

        } else if (booleans[0] == true && booleans[1] == true) {
            resultsNotFoundImageView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText("No Results Found!");
            resultNotFoundTextView.setVisibility(View.VISIBLE);
            retryImageView.setVisibility(View.GONE);
        }

        getLoaderManager().destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Boolean[]> loader) {

    }
}
