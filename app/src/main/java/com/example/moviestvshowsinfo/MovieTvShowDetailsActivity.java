package com.example.moviestvshowsinfo;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.moviestvshowsinfo.AsynTaskLoaderJSONPasrsing.MovieTvShowDetailsLoader;
import com.example.moviestvshowsinfo.AsynTaskLoaderJSONPasrsing.MovieTvShowYouTubeVideoLoader;
import com.example.moviestvshowsinfo.Database.AppDataBase;
import com.example.moviestvshowsinfo.Database.MovieTvShowEntity;
import com.example.moviestvshowsinfo.Database.MovieTvShowsDetailsViewModel;
import com.example.moviestvshowsinfo.Database.MovieTvShowsDetailsViewModelFactory;
import com.example.moviestvshowsinfo.Utils.Utils;
import com.example.moviestvshowsinfo.databinding.MovieTvShowDetailsDesignBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This Activity will be launched whenever the user presses on any item in the Recycler View.
 */
public class MovieTvShowDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static final int MOVIE_TV_SHOW_DETAILS_LOADER_ID = 1;
    private static final int YOUTUBE_VIDEO_LOADER_ID = 2;

    /**
     * Created an instance of {@MovieTvShowDetailsDesignBinding} instead of creating each instance for
     * every view.
     */
    private MovieTvShowDetailsDesignBinding mDataBinding;

    private Bitmap posterImageBitmap = null;
    private Bitmap backgroundImageBitmap = null;

    private String youTubeVideoKey = "";

    private MovieTvShowsDetails movieTvShowsDetails;

    private AppDataBase appDataBase;

    private int databaseMovieOrTvShowId = 0;

    private MenuItem favouriteMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_tv_show_details_design);

        mDataBinding = DataBindingUtil.setContentView(this, R.layout.movie_tv_show_details_design);

        setSupportActionBar(mDataBinding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        if (Utils.isMovie() || Utils.isFavouriteMovies()) {
            actionBar.setTitle("Movie Details");
        } else if (Utils.isTvShow() || Utils.isFavouriteTvShows()) {
            actionBar.setTitle("Tv Show Details");
        }


        mDataBinding.constraintLayout.setVisibility(View.GONE);
        mDataBinding.offlineOrNoResultTextView.setVisibility(View.GONE);
        mDataBinding.retryImageView.setVisibility(View.GONE);


        /**
         * Whenever the user clicks on the Youtube icon then launch the youtube app and
         * display the trailer.
         */
        mDataBinding.trailerIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (youTubeVideoKey != null || !youTubeVideoKey.isEmpty()) {
                    watchYoutubeVideo(youTubeVideoKey);
                }
            }
        });

        appDataBase = AppDataBase.getInstance(getApplicationContext());

        /**
         * Loading the Movie or Tv Show id  from the database and saving it into the {@databaseMovieorTvShowId}
         * variable to check whether the clicked movie or tv show is stored in the database or not.
         */
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                databaseMovieOrTvShowId = appDataBase.movieTvShowDao().loadId(Utils.getMovieOrTvShowId());
            }
        });


        if (Utils.isFavouriteTvShows() || Utils.isFavouriteMovies()) {

            /**
             * If the user opened any favourite movie or tv show then load youtube key from database not
             * the internet.
             */
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    youTubeVideoKey = appDataBase.movieTvShowDao().getYoutubeKey(databaseMovieOrTvShowId);
                }
            });


            MovieTvShowsDetailsViewModelFactory movieTvShowsDetailsViewModelFactory = new MovieTvShowsDetailsViewModelFactory(appDataBase, Utils.getMovieOrTvShowId());
            final MovieTvShowsDetailsViewModel movieTvShowsDetailsViewModel = ViewModelProviders
                    .of(this, movieTvShowsDetailsViewModelFactory)
                    .get(MovieTvShowsDetailsViewModel.class);


            /**
             * Using ViewModel to to load data from database and update data if any change in the database occurred.
             */
            movieTvShowsDetailsViewModel.getMovieTvShowEntityLiveData().observe(this, new Observer<MovieTvShowEntity>() {
                @Override
                public void onChanged(@Nullable MovieTvShowEntity movieTvShowEntity) {
                    movieTvShowsDetailsViewModel.getMovieTvShowEntityLiveData().removeObserver(this);
                    setDataIntoViewsFromDatabaseData(movieTvShowEntity);
                    mDataBinding.progressBar.setVisibility(View.GONE);
                    mDataBinding.constraintLayout.setVisibility(View.VISIBLE);
                }
            });

        } else {
            /**
             * If the user do not opened favourite movie or tv show then load youtube key and movie or tv show details
             * from the internet.
             */
            getSupportLoaderManager().restartLoader(YOUTUBE_VIDEO_LOADER_ID, null, this);
            getSupportLoaderManager().restartLoader(MOVIE_TV_SHOW_DETAILS_LOADER_ID, null, this);
        }

    }


    /**
     * This method will set data into views, if the data is loaded from internet
     */
    private void setDataIntoViewsFromInternetData(final MovieTvShowsDetails movieTvShowsDetails) {

        /**
         * {@favouriteMenuItem} is set to visible when the images are completely loaded because if the
         * user presses on {@favouriteMenuItem} while images are loading then the app the images will not be stored
         * in the database.
         */

        if (movieTvShowsDetails.getmBackgroundPath() != null && !movieTvShowsDetails.getmBackgroundPath().isEmpty()) {

            Target targetForBackGroundImage = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    mDataBinding.backgroundImageView.setImageBitmap(bitmap);
                    backgroundImageBitmap = bitmap;

                    if (movieTvShowsDetails.getmPosterPath() != null && !movieTvShowsDetails.getmPosterPath().isEmpty()) {
                        Picasso.get()
                                .load(movieTvShowsDetails.getmPosterPath())
                                .resize(314, 446)
                                .into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        posterImageBitmap = bitmap;
                                        favouriteMenuItem.setVisible(true);
                                    }

                                    @Override
                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                        favouriteMenuItem.setVisible(true);
                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                });
                    }else {
                        favouriteMenuItem.setVisible(true);
                    }
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            mDataBinding.backgroundImageView.setTag(targetForBackGroundImage);

            Picasso.get()
                    .load(movieTvShowsDetails.getmBackgroundPath())
                    .resize(824, 532)
                    .into(targetForBackGroundImage);


        } else if (movieTvShowsDetails.getmPosterPath() != null && !movieTvShowsDetails.getmPosterPath().isEmpty()) {

            Target target=new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    mDataBinding.backgroundImageView.setImageBitmap(bitmap);
                    Picasso.get()
                            .load(movieTvShowsDetails.getmPosterPath())
                            .resize(314,446)
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    posterImageBitmap = bitmap;
                                    favouriteMenuItem.setVisible(true);
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            mDataBinding.backgroundImageView.setTag(target);

            Picasso.get()
                    .load(movieTvShowsDetails.getmPosterPath())
                    .resize(824,532)
                    .into(target);

        } else {
            mDataBinding.backgroundImageView.setBackgroundColor(Color.BLACK);
            if (!movieTvShowsDetails.getmName().isEmpty()) {
                favouriteMenuItem.setVisible(true);
                mDataBinding.emptyBackgroundImageTextView.setVisibility(View.VISIBLE);
                mDataBinding.emptyBackgroundImageTextView.setText(movieTvShowsDetails.getmName());
            }

        }


        if (movieTvShowsDetails.getmName() != null && !movieTvShowsDetails.getmName().isEmpty()) {
            mDataBinding.nameTextView.setText(movieTvShowsDetails.getmName());
        }

        if (movieTvShowsDetails.getmGenres() != null && !movieTvShowsDetails.getmGenres().isEmpty()) {
            mDataBinding.genreTextView.setText(movieTvShowsDetails.getmGenres());
        } else {
            mDataBinding.genreTextView.setVisibility(View.GONE);
        }

        if (movieTvShowsDetails.getmOverView() != null && !movieTvShowsDetails.getmOverView().isEmpty()) {
            mDataBinding.overviewSummaryTextView.setText(movieTvShowsDetails.getmOverView());
        } else {
            mDataBinding.overviewTitleTextView.setVisibility(View.GONE);
            mDataBinding.overviewSummaryTextView.setVisibility(View.GONE);
        }

        if (movieTvShowsDetails.getmBudget() > 0) {
            mDataBinding.budgetSummaryTextView.setText(Integer.toString(movieTvShowsDetails.getmBudget()) + " $");
        } else {
            mDataBinding.budgetTitleTextView.setVisibility(View.GONE);
            mDataBinding.budgetSummaryTextView.setVisibility(View.GONE);
        }

        if (movieTvShowsDetails.getmRevenue() > 0) {
            mDataBinding.revenueSummaryTextView.setText(Integer.toString(movieTvShowsDetails.getmRevenue()) + " $");
        } else {
            mDataBinding.revenueTitleTextView.setVisibility(View.GONE);
            mDataBinding.revenueSummaryTextView.setVisibility(View.GONE);
        }

        if (movieTvShowsDetails.getmReleaseDate() != null && !movieTvShowsDetails.getmReleaseDate().isEmpty()) {
            if (Utils.isMovie()) {
                mDataBinding.releaseDateTitleTextView.setText("Release Date :");
            } else {
                mDataBinding.releaseDateTitleTextView.setText("First Air Date :");
            }
            mDataBinding.releaseDateSummaryTextView.setText(movieTvShowsDetails.getmReleaseDate());
        }

        if (movieTvShowsDetails.getmProductionCompanies() != null && !movieTvShowsDetails.getmProductionCompanies().isEmpty()) {
            mDataBinding.productionCompaniesSummaryTextView.setText(movieTvShowsDetails.getmProductionCompanies());
        } else {
            mDataBinding.productionCompaniesTitleTextView.setVisibility(View.INVISIBLE);
            mDataBinding.productionCompaniesSummaryTextView.setVisibility(View.GONE);
        }

        if (movieTvShowsDetails.getmAverageVote() > 0) {
            mDataBinding.averageVoteTextView.setText(Double.toString(movieTvShowsDetails.getmAverageVote()));
        } else {
            mDataBinding.imdbIconImageView.setVisibility(View.GONE);
            mDataBinding.averageVoteTextView.setVisibility(View.GONE);
        }

        if (movieTvShowsDetails.ismAdult()) {
            mDataBinding.adultIconImageView.setVisibility(View.VISIBLE);
        } else {
            mDataBinding.adultIconImageView.setVisibility(View.GONE);
        }

    }

    /**
     * This method will set data into views, if the data is loaded from database
     */
    private void setDataIntoViewsFromDatabaseData(MovieTvShowEntity movieTvShowEntity) {

        if (youTubeVideoKey == null || youTubeVideoKey.isEmpty()) {
            mDataBinding.trailerTextView.setVisibility(View.GONE);
            mDataBinding.trailerIconImageView.setVisibility(View.GONE);
        }

        if (movieTvShowEntity.getBackgroundImagePath() != null && !movieTvShowEntity.getBackgroundImagePath().isEmpty()) {

            Bitmap bitmap = BitmapFactory.decodeFile(movieTvShowEntity.getBackgroundImagePath());
            mDataBinding.backgroundImageView.setImageBitmap(bitmap);

        } else if (movieTvShowEntity.getPosterImagePath() != null && !movieTvShowEntity.getPosterImagePath().isEmpty()) {

            Bitmap bitmap = BitmapFactory.decodeFile(movieTvShowEntity.getPosterImagePath());
            mDataBinding.backgroundImageView.setImageBitmap(bitmap);
        } else {
            mDataBinding.backgroundImageView.setBackgroundColor(Color.BLACK);
            if (movieTvShowEntity.getMovieTitle() != null && !movieTvShowEntity.getMovieTitle().isEmpty()) {
                mDataBinding.emptyBackgroundImageTextView.setVisibility(View.VISIBLE);
                mDataBinding.emptyBackgroundImageTextView.setText(movieTvShowEntity.getMovieTitle());
            } else if (movieTvShowEntity.getTvShowName() != null && !movieTvShowEntity.getTvShowName().isEmpty()) {
                mDataBinding.emptyBackgroundImageTextView.setVisibility(View.VISIBLE);
                mDataBinding.emptyBackgroundImageTextView.setText(movieTvShowEntity.getTvShowName());
            }

        }


        if (movieTvShowEntity.getMovieTitle() != null && !movieTvShowEntity.getMovieTitle().isEmpty()) {
            mDataBinding.nameTextView.setText(movieTvShowEntity.getMovieTitle());
        } else if (movieTvShowEntity.getTvShowName() != null && !movieTvShowEntity.getTvShowName().isEmpty()) {
            mDataBinding.nameTextView.setText(movieTvShowEntity.getTvShowName());
        }

        if (movieTvShowEntity.getGenres() != null && !movieTvShowEntity.getGenres().isEmpty()) {
            mDataBinding.genreTextView.setText(movieTvShowEntity.getGenres());
        } else {
            mDataBinding.genreTextView.setVisibility(View.GONE);
        }

        if (movieTvShowEntity.getOverview() != null && !movieTvShowEntity.getOverview().isEmpty()) {
            mDataBinding.overviewSummaryTextView.setText(movieTvShowEntity.getOverview());
        }

        if (movieTvShowEntity.getBudget() > 0) {
            mDataBinding.budgetSummaryTextView.setText(Integer.toString(movieTvShowEntity.getBudget()) + " $");
        } else {
            mDataBinding.budgetTitleTextView.setVisibility(View.GONE);
            mDataBinding.budgetSummaryTextView.setVisibility(View.GONE);
        }

        if (movieTvShowEntity.getRevenue() > 0) {
            mDataBinding.revenueSummaryTextView.setText(Integer.toString(movieTvShowEntity.getRevenue()) + " $");
        } else {
            mDataBinding.revenueTitleTextView.setVisibility(View.GONE);
            mDataBinding.revenueSummaryTextView.setVisibility(View.GONE);
        }

        if (movieTvShowEntity.getMovieReleaseDate() != null && !movieTvShowEntity.getMovieReleaseDate().isEmpty()) {
            mDataBinding.releaseDateTitleTextView.setText("Release Date:");
            mDataBinding.releaseDateSummaryTextView.setText(movieTvShowEntity.getMovieReleaseDate());
        } else if (movieTvShowEntity.getTvShowFirstAirDate() != null && !movieTvShowEntity.getTvShowFirstAirDate().isEmpty()) {
            mDataBinding.releaseDateTitleTextView.setText("First Air Date:");
            mDataBinding.releaseDateSummaryTextView.setText(movieTvShowEntity.getTvShowFirstAirDate());
        }

        if (movieTvShowEntity.getProductionCompanies() != null && !movieTvShowEntity.getProductionCompanies().isEmpty()) {
            mDataBinding.productionCompaniesSummaryTextView.setText(movieTvShowEntity.getProductionCompanies());
        } else {
            mDataBinding.productionCompaniesTitleTextView.setVisibility(View.INVISIBLE);
            mDataBinding.productionCompaniesSummaryTextView.setVisibility(View.GONE);
        }

        if (movieTvShowEntity.getVoteAverage() > 0) {
            mDataBinding.averageVoteTextView.setText(Double.toString(movieTvShowEntity.getVoteAverage()));
        } else {
            mDataBinding.imdbIconImageView.setVisibility(View.GONE);
            mDataBinding.averageVoteTextView.setVisibility(View.GONE);
        }

        if (movieTvShowEntity.isAdult()) {
            mDataBinding.adultIconImageView.setVisibility(View.VISIBLE);
        } else {
            mDataBinding.adultIconImageView.setVisibility(View.GONE);
        }

    }

    /**
     * This method will get the youtube key.
     * If the Youtube App is installed then start the youtube app and play trailer
     * otherwise play trailer on browser.
     */
    public void watchYoutubeVideo(String key) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    /**
     * This method will insert the data into database.
     */
    private MovieTvShowEntity insertDataIntoDataBase() {

        String backgroundPath = null;
        String posterPath = null;

        if (backgroundImageBitmap != null) {
            backgroundPath = createProductImageFile(backgroundImageBitmap);
        }
        if (posterImageBitmap != null) {
            posterPath = createProductImageFile(posterImageBitmap);
        }

        String title = null;
        String name = null;
        String releaseDate = null;
        String firstAirDate = null;

        if (Utils.isMovie()) {
            title = movieTvShowsDetails.getmName();
            releaseDate = movieTvShowsDetails.getmReleaseDate();
            name = null;
            firstAirDate = null;
        } else if (Utils.isTvShow()) {
            name = movieTvShowsDetails.getmName();
            firstAirDate = movieTvShowsDetails.getmReleaseDate();
            title = null;
            releaseDate = null;
        }


        MovieTvShowEntity movieTvShowEntity = new MovieTvShowEntity(movieTvShowsDetails.getmId(),
                title,
                name,
                movieTvShowsDetails.getmLanguage(),
                releaseDate,
                firstAirDate,
                movieTvShowsDetails.ismAdult(),
                movieTvShowsDetails.getmAverageVote(),
                posterPath,
                backgroundPath,
                movieTvShowsDetails.getmGenres(),
                movieTvShowsDetails.getmOverView(),
                movieTvShowsDetails.getmBudget(),
                movieTvShowsDetails.getmRevenue(),
                movieTvShowsDetails.getmProductionCompanies(),
                youTubeVideoKey);


        return movieTvShowEntity;
    }


    @NonNull
    @Override
    public Loader onCreateLoader(int i, @Nullable Bundle bundle) {

        switch (i) {
            case MOVIE_TV_SHOW_DETAILS_LOADER_ID:
                mDataBinding.progressBar.setVisibility(View.VISIBLE);
                mDataBinding.constraintLayout.setVisibility(View.INVISIBLE);

                if (Utils.isMovie()) {

                    String url = Utils.GET_DETAILS_MOVIES_STARTING_URL + Utils.getMovieOrTvShowId() + Utils.GET_DETAILS_MOVIES_ENDING_URL;
                    return new MovieTvShowDetailsLoader(this, url);

                } else if (Utils.isTvShow()) {
                    String url = Utils.GET_DETAILS_TV_SHOWS_STARTING_URL + Utils.getMovieOrTvShowId() + Utils.GET_DETAILS_TV_SHOWS_ENDING_URL;
                    return new MovieTvShowDetailsLoader(this, url);
                }
                break;

            case YOUTUBE_VIDEO_LOADER_ID:
                String url = Utils.YOUTUBE_GET_VIDEOS_STARTING_URL + Utils.getMovieOrTvShowId() + Utils.YOUTUBE_GET_VIDEOS_ENDING_URL;
                return new MovieTvShowYouTubeVideoLoader(this, url);
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object o) {

        int id = loader.getId();

        if (id == YOUTUBE_VIDEO_LOADER_ID) {
            if (o != null) {
                youTubeVideoKey = (String) o;
                /**
                 * If the Youtube Video key is empty or null then make the {@trailerTextView} and {@trailerIconImageView}
                 * to GONE.
                 */
                if (youTubeVideoKey == null || youTubeVideoKey.isEmpty()) {
                    mDataBinding.trailerTextView.setVisibility(View.GONE);
                    mDataBinding.trailerIconImageView.setVisibility(View.GONE);
                } else {
                    mDataBinding.trailerTextView.setVisibility(View.VISIBLE);
                    mDataBinding.trailerIconImageView.setVisibility(View.VISIBLE);
                }
            }
        }

        if (id == MOVIE_TV_SHOW_DETAILS_LOADER_ID) {
            if (o != null) {
                /**
                 * If the MovieTvShow details is not null then set visibility of views for online data
                 * by calling {@setVisibilityOfViewForOnline} method.
                 */
                setVisibilityOfViewForOnline();
                setDataIntoViewsFromInternetData((MovieTvShowsDetails) o);
                movieTvShowsDetails = (MovieTvShowsDetails) o;
                //favouriteMenuItem.setVisible(true);

            } else {

                /**
                 * If the MovieTvShow details is null then set visibility of {@favouriteMenuItem} to false and
                 * set visibility of views to for offline data by calling {@setVisibilityOfViewForOffline} method.
                 */
                favouriteMenuItem.setVisible(false);
                setVisibilityOfViewForOffline();
                /**
                 * If the user clicks on the retry button then restart both loaders:
                 * 1-YOUTUBE_VIDEO_LOADER
                 * 2-MOVIE_TV_SHOW_DETAILS_LOADER
                 */
                mDataBinding.retryImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSupportLoaderManager().restartLoader(YOUTUBE_VIDEO_LOADER_ID, null, MovieTvShowDetailsActivity.this);
                        getSupportLoaderManager().restartLoader(MOVIE_TV_SHOW_DETAILS_LOADER_ID, null, MovieTvShowDetailsActivity.this);

                    }
                });
            }
        }
        mDataBinding.progressBar.setVisibility(View.GONE);
        getSupportLoaderManager().destroyLoader(loader.getId());

    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_to_favourite_menu, menu);
        favouriteMenuItem = (MenuItem) menu.findItem(R.id.add_to_favourite);

        /**
         *Initially setting the visibility of {@favouriteMenuItem} to true if it is favourite movie or tv show
         * otherwise set to false because if the user clicks on the {@favouriteMenuItem} while the
         * Movie or Tv Show details are loading then the app will crash.
         */
        if (Utils.isFavouriteMovies() || Utils.isFavouriteTvShows()) {
            favouriteMenuItem.setVisible(true);
        } else {
            favouriteMenuItem.setVisible(false);
        }

        if (databaseMovieOrTvShowId == Utils.getMovieOrTvShowId()) {
            favouriteMenuItem.setIcon(R.drawable.ic_star_filled_black_24dp);
        } else {
            favouriteMenuItem.setIcon(R.drawable.ic_star_border_black_24dp);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add_to_favourite:
                if (item.getIcon().getConstantState().equals(getResources().getDrawable(R.drawable.ic_star_border_black_24dp).getConstantState())) {

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            appDataBase.movieTvShowDao().insertMovieTvShow(insertDataIntoDataBase());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    favouritesAddOrDeleteDialog();
                                    favouriteMenuItem.setIcon(R.drawable.ic_star_filled_black_24dp);
                                }
                            });
                        }

                    });
                    return true;
                } else {
                    alertDialogForAddingOrDeletingMovieOrTvShowFromFavourites();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * This method only displays a dialog whenever the user add or delete movie or tv show
     * from  the favourites.
     */
    private void favouritesAddOrDeleteDialog(){

        final Dialog dialog=new Dialog(this,R.style.AddedOrDeletedFromFavourites);
        View view=LayoutInflater.from(this).inflate(R.layout.favourites_dialog,null);

        TextView textView=(TextView) view.findViewById(R.id.dialog_text_view);
        if(favouriteMenuItem.getIcon().getConstantState().equals(getResources().getDrawable(R.drawable.ic_star_border_black_24dp)
                .getConstantState())){
            textView.setText("Added to Favourites!\nYou can also see it also offline.");
        }else {
            textView.setText("Deleted from Favourites!");
        }

        dialog.setContentView(view);
        dialog.show();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        },2000);

    }


    /**
     * This method sets the visibility of views for online data.
     */
    private void setVisibilityOfViewForOnline() {

        mDataBinding.constraintLayout.setVisibility(View.VISIBLE);

        mDataBinding.backgroundImageView.setVisibility(View.VISIBLE);
        mDataBinding.nameTextView.setVisibility(View.VISIBLE);
        mDataBinding.genreTextView.setVisibility(View.VISIBLE);
        mDataBinding.overviewTitleTextView.setVisibility(View.VISIBLE);
        mDataBinding.overviewSummaryTextView.setVisibility(View.VISIBLE);
        mDataBinding.budgetTitleTextView.setVisibility(View.VISIBLE);
        mDataBinding.budgetSummaryTextView.setVisibility(View.VISIBLE);
        mDataBinding.revenueTitleTextView.setVisibility(View.VISIBLE);
        mDataBinding.revenueSummaryTextView.setVisibility(View.VISIBLE);
        mDataBinding.releaseDateTitleTextView.setVisibility(View.VISIBLE);
        mDataBinding.releaseDateSummaryTextView.setVisibility(View.VISIBLE);
        mDataBinding.productionCompaniesTitleTextView.setVisibility(View.VISIBLE);
        mDataBinding.productionCompaniesSummaryTextView.setVisibility(View.VISIBLE);
        mDataBinding.imdbIconImageView.setVisibility(View.VISIBLE);
        mDataBinding.averageVoteTextView.setVisibility(View.VISIBLE);
        mDataBinding.adultIconImageView.setVisibility(View.VISIBLE);
        if (Utils.isMovie() && youTubeVideoKey != null && !youTubeVideoKey.isEmpty()) {
            mDataBinding.trailerTextView.setVisibility(View.VISIBLE);
            mDataBinding.trailerTextView.setVisibility(View.VISIBLE);
        } else if (Utils.isTvShow()) {
            mDataBinding.trailerTextView.setVisibility(View.GONE);
            mDataBinding.trailerIconImageView.setVisibility(View.GONE);
        }

        mDataBinding.offlineOrNoResultTextView.setVisibility(View.GONE);
        mDataBinding.retryImageView.setVisibility(View.GONE);

    }


    /**
     * This method sets the visibility of views for offline data.
     */
    private void setVisibilityOfViewForOffline() {


        mDataBinding.constraintLayout.setVisibility(View.VISIBLE);

        mDataBinding.progressBar.setVisibility(View.INVISIBLE);
        mDataBinding.backgroundImageView.setVisibility(View.INVISIBLE);
        mDataBinding.nameTextView.setVisibility(View.INVISIBLE);
        mDataBinding.genreTextView.setVisibility(View.INVISIBLE);
        mDataBinding.overviewTitleTextView.setVisibility(View.INVISIBLE);
        mDataBinding.overviewSummaryTextView.setVisibility(View.INVISIBLE);
        mDataBinding.budgetTitleTextView.setVisibility(View.INVISIBLE);
        mDataBinding.budgetSummaryTextView.setVisibility(View.INVISIBLE);
        mDataBinding.revenueTitleTextView.setVisibility(View.INVISIBLE);
        mDataBinding.revenueSummaryTextView.setVisibility(View.INVISIBLE);
        mDataBinding.releaseDateTitleTextView.setVisibility(View.INVISIBLE);
        mDataBinding.releaseDateSummaryTextView.setVisibility(View.INVISIBLE);
        mDataBinding.productionCompaniesTitleTextView.setVisibility(View.INVISIBLE);
        mDataBinding.productionCompaniesSummaryTextView.setVisibility(View.INVISIBLE);
        mDataBinding.imdbIconImageView.setVisibility(View.INVISIBLE);
        mDataBinding.averageVoteTextView.setVisibility(View.INVISIBLE);
        mDataBinding.adultIconImageView.setVisibility(View.INVISIBLE);
        mDataBinding.trailerTextView.setVisibility(View.GONE);
        mDataBinding.trailerIconImageView.setVisibility(View.GONE);

        mDataBinding.offlineOrNoResultTextView.setVisibility(View.VISIBLE);
        mDataBinding.retryImageView.setVisibility(View.VISIBLE);
    }


    private String posterImagePath = "";
    private String backgroundImagePath = "";

    /**
     * This method contains all the code from displaying Alert Dialog to deleting Movie or Tv Show.
     */
    private void alertDialogForAddingOrDeletingMovieOrTvShowFromFavourites() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_dialog_favourites_message)
                .setPositiveButton(R.string.alert_dialog_favourites_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                posterImagePath = appDataBase.movieTvShowDao().loadPosterImage(Utils.getMovieOrTvShowId());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (posterImagePath != null && !posterImagePath.isEmpty()) {
                                            File posterImageFile = new File(posterImagePath);
                                            posterImageFile.delete();
                                        }
                                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                backgroundImagePath = appDataBase.movieTvShowDao().loadBackgroundImage(Utils.getMovieOrTvShowId());
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (backgroundImagePath != null && !backgroundImagePath.isEmpty()) {
                                                            File backgroundImageFile = new File(backgroundImagePath);
                                                            backgroundImageFile.delete();
                                                        }
                                                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                appDataBase.movieTvShowDao().deleteSingleMovieOrTvShow(Utils.getMovieOrTvShowId());
                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        if (Utils.isFavouriteMovies() || Utils.isFavouriteTvShows()) {
                                                                            onBackPressed();
                                                                        }else {
                                                                            favouritesAddOrDeleteDialog();
                                                                        }
                                                                        favouriteMenuItem.setIcon(R.drawable.ic_star_border_black_24dp);
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });

                    }
                })
                .setNegativeButton(R.string.alert_dialog_favourites_negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * This method takes a bitmap and create an Image File and returns the Path of that Image File
     * in string.
     */
    private String createProductImageFile(Bitmap bitmap) {

        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir("Pictures", Context.MODE_PRIVATE);

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File myPath = new File(directory, imageFileName + ".jpg");

        String imagePath = myPath.getAbsolutePath();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imagePath;
    }
}

