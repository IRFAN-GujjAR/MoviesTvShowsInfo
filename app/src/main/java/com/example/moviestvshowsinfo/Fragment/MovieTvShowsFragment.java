package com.example.moviestvshowsinfo.Fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.moviestvshowsinfo.AsynTaskLoaderJSONPasrsing.MovieTvShowListLoader;
import com.example.moviestvshowsinfo.Database.AppDataBase;
import com.example.moviestvshowsinfo.Database.MovieTvShowEntity;
import com.example.moviestvshowsinfo.Database.MovieTvShowsViewModel;
import com.example.moviestvshowsinfo.Database.MovieTvShowsViewModelFactory;
import com.example.moviestvshowsinfo.MovieTvShowDetailsActivity;
import com.example.moviestvshowsinfo.MovieTvShowsDetails;
import com.example.moviestvshowsinfo.Adapters.MoviesTvShowsAdapter;
import com.example.moviestvshowsinfo.R;
import com.example.moviestvshowsinfo.Utils.Utils;

import java.util.List;

public class MovieTvShowsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<MovieTvShowsDetails>>,
        MoviesTvShowsAdapter.ListItemClickListener {

    private static final int MOVIES_TV_SHOWS_LIST_LOADER_ID = 1;
    private static final int SCROLL_MOVIES_TV_SHOWS_LIST_LOADER_ID = 2;


    /**
     * Creating instances of recycler view and all other instances associated with it.
     */
    private MoviesTvShowsAdapter moviesTvShowsAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;

    /**
     * These two variables will be used to check whether Mobile or Tablet is in Portrait or Landscape mode.
     */
    private boolean isPortrait = false;
    private boolean isLandscape = false;

    /**
     * These two variables will be used to check app is running on Tablet or Mobile.
     */
    private boolean isSmallScreenMobile = false;
    private boolean isTabletLayout = false;


    private ProgressBar progressBar;
    private ProgressBar loadMoreItemsProgressBar;

    /**
     * These two lines attributes are associated with the scrolling of recycler view.
     */
    private Boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems;

    /**
     * Initializing the pageNumber to 1 which will be used in the Url for JSON Parsing.
     */
    private int pageNumber = 1;

    /**
     * This string will store the current url.
     */
    private String mCurrentUrl;

    private AppDataBase appDataBase;


    public MovieTvShowsFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recycler_layout_fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) view.findViewById(R.id.movie_tv_show_list_progress_bar);
        loadMoreItemsProgressBar = (ProgressBar) view.findViewById(R.id.movie_tv_show_load_more_list_progress_bar);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);

        /**
         * Checking whether app is running on Tablet(screen inches equal to or greater than 9.5)
         *or on small Screen Mobile(screen inches less than 4.9)
         */
        if (Utils.getDisplayMetrics(getContext()) >= 9.5) {
            isTabletLayout = true;
        } else if (Utils.getDisplayMetrics(getContext()) < 4.9) {
            isSmallScreenMobile = true;
        }

        onConfigurationChanged(getActivity().getResources().getConfiguration());

        recyclerView.setHasFixedSize(true);
        moviesTvShowsAdapter = new MoviesTvShowsAdapter(this);
        recyclerView.setAdapter(moviesTvShowsAdapter);

        /**
         * If the user press on other than favourite movies or tv shows bottomNavigation Button
         * then attach {@addOnScrollListener} to recycler view and initialize the loader to load the data from internet.
         * If the user press on favourite movies or tv shows bottomNavigation Button then load
         * the data from DataBase.
         */
        if (!Utils.isFavouriteMovies() && !Utils.isFavouriteTvShows()) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isScrolling = true;
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if (isSmallScreenMobile) {
                        currentItems = linearLayoutManager.getChildCount();
                        totalItems = linearLayoutManager.getItemCount();
                        scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();
                    } else {

                        if (isPortrait) {
                            currentItems = linearLayoutManager.getChildCount();
                            totalItems = linearLayoutManager.getItemCount();
                            scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();
                        } else if (isLandscape || isTabletLayout) {
                            currentItems = gridLayoutManager.getChildCount();
                            totalItems = gridLayoutManager.getItemCount();
                            scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();
                        }
                    }

                    if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                        isScrolling = false;
                        loadMoreItemsProgressBar.setVisibility(View.VISIBLE);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getLoaderManager().restartLoader(SCROLL_MOVIES_TV_SHOWS_LIST_LOADER_ID, null, MovieTvShowsFragment.this);
                            }
                        }, 1000);
                    }
                }
            });

            getLoaderManager().initLoader(MOVIES_TV_SHOWS_LIST_LOADER_ID, null, this);


        } else {


            appDataBase = AppDataBase.getInstance(getContext().getApplicationContext());

            /**
             * Using ViewModel to load data from database and observe any change in database
             */
            MovieTvShowsViewModelFactory movieTvShowsViewModelFactory = new MovieTvShowsViewModelFactory(appDataBase);

            final MovieTvShowsViewModel movieTvShowsViewModel = ViewModelProviders
                    .of(this, movieTvShowsViewModelFactory)
                    .get(MovieTvShowsViewModel.class);

            movieTvShowsViewModel.getMovieTvShowsEntities().observe(this, new Observer<List<MovieTvShowEntity>>() {
                @Override
                public void onChanged(@Nullable List<MovieTvShowEntity> movieTvShowEntities) {
                    moviesTvShowsAdapter.setListInfoFromDatabaseData(movieTvShowEntities);

                    if (movieTvShowEntities.isEmpty()) {
                        Utils.setEmptyMovieTvShowList(true);
                        Utils.screenSlidePagerAdpater.notifyDataChanged();
                    }
                }
            });

        }


        return view;
    }


    @NonNull
    @Override
    public Loader<List<MovieTvShowsDetails>> onCreateLoader(int i, @Nullable Bundle bundle) {

        if (i == MOVIES_TV_SHOWS_LIST_LOADER_ID) {

            /**
             * Saving the url in the {@mCurrentUrl} and creating a uri in which page number is appended.
             */

            mCurrentUrl = Utils.getCurrentUrl();

            Uri uri = Uri.parse(mCurrentUrl);
            Uri.Builder builder = uri.buildUpon();
            builder.appendQueryParameter("page", Integer.toString(pageNumber));
            Utils.setCurrentUrl(builder.toString());

            progressBar.setVisibility(View.VISIBLE);

        } else if (i == SCROLL_MOVIES_TV_SHOWS_LIST_LOADER_ID) {

            /**
             * If the user scrolled then increment the page number and append it to uri.
             */
            pageNumber++;

            Uri uri = Uri.parse(mCurrentUrl);
            Uri.Builder builder = uri.buildUpon();
            builder.appendQueryParameter("page", Integer.toString(pageNumber));
            Utils.setCurrentUrl(builder.toString());

            loadMoreItemsProgressBar.setVisibility(View.VISIBLE);
        }
        return new MovieTvShowListLoader(getContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<MovieTvShowsDetails>> loader, List<MovieTvShowsDetails> movieTvShowsDetails) {

        if (movieTvShowsDetails != null) {

            /**
             * Updating the adapter with the {@movieTvShowsDetails} list.
             */
            moviesTvShowsAdapter.setListInfoFromInternetData(movieTvShowsDetails);
            progressBar.setVisibility(View.GONE);
            loadMoreItemsProgressBar.setVisibility(View.GONE);

            getLoaderManager().destroyLoader(loader.getId());
        } else {
            progressBar.setVisibility(View.GONE);
            loadMoreItemsProgressBar.setVisibility(View.GONE);

            if (pageNumber == 1) {
                getLoaderManager().destroyLoader(loader.getId());
                /**
                 * Setting the {@emptyMovieTvShowList} to true and notifying the {@screenSlidPagerAdapter} for change
                 */
                Utils.setEmptyMovieTvShowList(true);
                Utils.screenSlidePagerAdpater.notifyDataChanged();
            } else {
                Toast.makeText(getContext(), "No more results found!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<MovieTvShowsDetails>> loader) {

    }

    /**
     * This method is called when user presses on one of the recycler view item.
     */
    @Override
    public void onListItemClick(int id) {
        Utils.setMovieOrTvShowId(id);
        Intent intent = new Intent(getActivity(), MovieTvShowDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            /**
             * If the app is running on Tablet(screenInches>=9.5) then set recycler view layout to gridLayout
             * otherwise set to linearLayout.
             */
            if (isTabletLayout) {
                recyclerView.setLayoutManager(gridLayoutManager);
            } else {
                isPortrait = true;
                isLandscape = false;
                recyclerView.setLayoutManager(linearLayoutManager);
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            isPortrait = false;
            isLandscape = true;
            /**
             * If the app is running on small screen mobile(screenInches<4.9)
             * then set recycler view layout to linearLayout otherwise set to gridLayout.
             */
            if (isSmallScreenMobile) {
                recyclerView.setLayoutManager(linearLayoutManager);
            } else {
                recyclerView.setLayoutManager(gridLayoutManager);
            }
        }
        super.onConfigurationChanged(newConfig);
    }
}
