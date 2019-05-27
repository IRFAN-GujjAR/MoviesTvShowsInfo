package com.example.moviestvshowsinfo.Adapters;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviestvshowsinfo.Database.MovieTvShowEntity;
import com.example.moviestvshowsinfo.MovieTvShowsDetails;
import com.example.moviestvshowsinfo.R;
import com.example.moviestvshowsinfo.Utils.Utils;
import com.example.moviestvshowsinfo.databinding.MovieTvShowListItemDesignBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesTvShowsAdapter extends RecyclerView.Adapter<MoviesTvShowsAdapter.MovieViewsHolder> {


    /**
     * This field will contain the list of data gathered from internet.
     */
    private List<MovieTvShowsDetails> moviesTvShowsDetailsList;

    /**
     * This field will contain the list of data gathered from database.
     */
    private List<MovieTvShowEntity> moviesTvShowsEntitiesList;

    private int numberOfItem = 0;

    private int itemNumber;

    /**
     * We will implement this interface in {@MovieTvShowsFragment} to get the id of Movie or Tv Show.
     */
    public interface ListItemClickListener {
        void onListItemClick(int id);
    }

    private final ListItemClickListener listItemClickListener;

    public MoviesTvShowsAdapter(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
        moviesTvShowsDetailsList = null;
        moviesTvShowsEntitiesList = null;
        itemNumber = 0;
    }

    @NonNull
    @Override
    public MovieViewsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_tv_show_list_item_design, viewGroup, false);
        MovieViewsHolder movieViewsHolder = new MovieViewsHolder(view);

        /**
         * This if statement contains all code of displaying the data gathered from internet.
         */
        if (moviesTvShowsDetailsList != null) {

            MovieTvShowsDetails currentMovieTvShowsDetails = moviesTvShowsDetailsList.get(itemNumber);


            if (currentMovieTvShowsDetails.getmPosterPath() != null && !currentMovieTvShowsDetails.getmPosterPath().isEmpty()) {
                Picasso.get()
                        .load(currentMovieTvShowsDetails.getmPosterPath())
                        .resize(314, 446)
                        .into(movieViewsHolder.listItemDesignBinding.posterImageView);
            } else if (currentMovieTvShowsDetails.getmBackgroundPath() != null && !currentMovieTvShowsDetails.getmBackgroundPath().isEmpty()) {
                Picasso.get()
                        .load(currentMovieTvShowsDetails.getmBackgroundPath())
                        .resize(314, 446)
                        .into(movieViewsHolder.listItemDesignBinding.posterImageView);

            } else {
                movieViewsHolder.listItemDesignBinding.posterImageView.setBackgroundColor(Color.BLACK);
                if (currentMovieTvShowsDetails.getmName() != null && !currentMovieTvShowsDetails.getmName().isEmpty()) {
                    movieViewsHolder.listItemDesignBinding.emptyPosterImageTextView.setVisibility(View.VISIBLE);
                    movieViewsHolder.listItemDesignBinding.emptyPosterImageTextView.setText(currentMovieTvShowsDetails.getmName());
                }
            }

            if (currentMovieTvShowsDetails.getmName() != null && !currentMovieTvShowsDetails.getmName().isEmpty()) {
                movieViewsHolder.listItemDesignBinding.nameTextView.setText(currentMovieTvShowsDetails.getmName());
            }

            if (currentMovieTvShowsDetails.getmGenres() != null && !currentMovieTvShowsDetails.getmGenres().isEmpty()) {
                movieViewsHolder.listItemDesignBinding.genreTextView.setText(currentMovieTvShowsDetails.getmGenres());
            }

            if (currentMovieTvShowsDetails.getmLanguage() != null && !currentMovieTvShowsDetails.getmLanguage().isEmpty()) {
                movieViewsHolder.listItemDesignBinding.languageSummaryTextView.setText(currentMovieTvShowsDetails.getmLanguage());
            }

            if (Utils.isMovie()) {
                movieViewsHolder.listItemDesignBinding.releaseDateTitleTextView.setText("Release Date :");
            } else if (Utils.isTvShow()) {
                movieViewsHolder.listItemDesignBinding.releaseDateTitleTextView.setText("First Air Date :");
            }

            if (currentMovieTvShowsDetails.getmReleaseDate() != null && !currentMovieTvShowsDetails.getmReleaseDate().isEmpty()) {
                movieViewsHolder.listItemDesignBinding.releaseDateSummaryTextView.setText(currentMovieTvShowsDetails.getmReleaseDate());
            }

            if (currentMovieTvShowsDetails.getmAverageVote() > 0) {
                movieViewsHolder.listItemDesignBinding.voteAverageTextView.setText(Double.toString(currentMovieTvShowsDetails.getmAverageVote()));
            } else {
                movieViewsHolder.listItemDesignBinding.voteAverageTextView.setText("Not Rated Yet!");
            }
            if (currentMovieTvShowsDetails.ismAdult()) {
                movieViewsHolder.listItemDesignBinding.adultIconImageView.setVisibility(View.VISIBLE);
            } else {
                movieViewsHolder.listItemDesignBinding.adultIconImageView.setVisibility(View.INVISIBLE);
            }

            if (itemNumber < numberOfItem) {
                itemNumber++;
            }
        }


        return movieViewsHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MovieViewsHolder movieViewsHolder, int i) {

        /**
         * This if statement contains all code of displaying the data gathered from database.
         */

        if (moviesTvShowsEntitiesList != null) {

            MovieTvShowEntity movieTvShowEntity = moviesTvShowsEntitiesList.get(itemNumber);

            if (movieTvShowEntity.getPosterImagePath() != null && !movieTvShowEntity.getPosterImagePath().isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(movieTvShowEntity.getPosterImagePath());
                movieViewsHolder.listItemDesignBinding.posterImageView.setImageBitmap(bitmap);

            } else if (movieTvShowEntity.getBackgroundImagePath() != null && !movieTvShowEntity.getBackgroundImagePath().isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(movieTvShowEntity.getBackgroundImagePath());
                movieViewsHolder.listItemDesignBinding.posterImageView.setImageBitmap(bitmap);
            } else {
                movieViewsHolder.listItemDesignBinding.posterImageView.setBackgroundColor(Color.BLACK);
                if (movieTvShowEntity.getMovieTitle() != null && !movieTvShowEntity.getMovieTitle().isEmpty()) {
                    movieViewsHolder.listItemDesignBinding.emptyPosterImageTextView.setVisibility(View.VISIBLE);
                    movieViewsHolder.listItemDesignBinding.emptyPosterImageTextView.setText(movieTvShowEntity.getMovieTitle());
                } else if (movieTvShowEntity.getTvShowName() != null && !movieTvShowEntity.getTvShowName().isEmpty()) {
                    movieViewsHolder.listItemDesignBinding.emptyPosterImageTextView.setVisibility(View.VISIBLE);
                    movieViewsHolder.listItemDesignBinding.emptyPosterImageTextView.setText(movieTvShowEntity.getTvShowName());
                }
            }

            if (movieTvShowEntity.getMovieTitle() != null && !movieTvShowEntity.getMovieTitle().isEmpty()) {
                movieViewsHolder.listItemDesignBinding.nameTextView.setText(movieTvShowEntity.getMovieTitle());
            } else if (movieTvShowEntity.getTvShowName() != null && !movieTvShowEntity.getTvShowName().isEmpty()) {
                movieViewsHolder.listItemDesignBinding.nameTextView.setText(movieTvShowEntity.getTvShowName());
            }

            if (movieTvShowEntity.getGenres() != null && !movieTvShowEntity.getGenres().isEmpty()) {
                movieViewsHolder.listItemDesignBinding.genreTextView.setText(movieTvShowEntity.getGenres());
            }

            if (movieTvShowEntity.getOriginalLanguage() != null && !movieTvShowEntity.getOriginalLanguage().isEmpty()) {
                movieViewsHolder.listItemDesignBinding.languageSummaryTextView.setText(movieTvShowEntity.getOriginalLanguage());
            }

            if (Utils.isFavouriteMovies()) {
                movieViewsHolder.listItemDesignBinding.releaseDateTitleTextView.setText("Release Date :");
            } else if (Utils.isFavouriteTvShows()) {
                movieViewsHolder.listItemDesignBinding.releaseDateTitleTextView.setText("First Air Date :");
            }

            if (movieTvShowEntity.getMovieReleaseDate() != null && !movieTvShowEntity.getMovieReleaseDate().isEmpty()) {
                movieViewsHolder.listItemDesignBinding.releaseDateSummaryTextView.setText(movieTvShowEntity.getMovieReleaseDate());
            } else if (movieTvShowEntity.getTvShowFirstAirDate() != null && !movieTvShowEntity.getTvShowFirstAirDate().isEmpty()) {
                movieViewsHolder.listItemDesignBinding.releaseDateSummaryTextView.setText(movieTvShowEntity.getTvShowFirstAirDate());
            }

            if (movieTvShowEntity.getVoteAverage() > 0) {
                movieViewsHolder.listItemDesignBinding.voteAverageTextView.setText(Double.toString(movieTvShowEntity.getVoteAverage()));
            } else {
                movieViewsHolder.listItemDesignBinding.voteAverageTextView.setText("Not Rated Yet!");
            }
            if (movieTvShowEntity.isAdult()) {
                movieViewsHolder.listItemDesignBinding.adultIconImageView.setVisibility(View.VISIBLE);
            } else {
                movieViewsHolder.listItemDesignBinding.adultIconImageView.setVisibility(View.INVISIBLE);
            }

            if (itemNumber < numberOfItem) {
                itemNumber++;
            }
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return numberOfItem;
    }

    /**
     * This method will refresh the adapter data for list from internet.
     */
    public void setListInfoFromInternetData(List<MovieTvShowsDetails> listData) {
        if (listData != null) {
            if (moviesTvShowsDetailsList == null) {
                moviesTvShowsDetailsList = listData;
            } else {
                moviesTvShowsDetailsList.addAll(listData);
            }

            numberOfItem = moviesTvShowsDetailsList.size();
        } else {
            numberOfItem = 0;
        }
        notifyDataSetChanged();
    }

    /**
     * This method will refresh the adapter data for list from database.
     */
    public void setListInfoFromDatabaseData(List<MovieTvShowEntity> movieTvShowEntities) {

        itemNumber = 0;
        moviesTvShowsEntitiesList = movieTvShowEntities;
        numberOfItem = moviesTvShowsEntitiesList.size();
        notifyDataSetChanged();
    }

    class MovieViewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /**
         * Creating an instance of {@MovieTvShowListItemDesignBidning} instead of creating number of
         * instances for multiple views.
         */
        private MovieTvShowListItemDesignBinding listItemDesignBinding;

        public MovieViewsHolder(@NonNull View itemView) {
            super(itemView);

            listItemDesignBinding= DataBindingUtil.bind(itemView);

            itemView.setOnClickListener(this);
        }


        /**
         *{@onClick} method will be called whenever the user presses on an item in recyclerView.
         */
        @Override
        public void onClick(View v) {

            if (moviesTvShowsDetailsList == null) {
                MovieTvShowEntity movieTvShowEntity = moviesTvShowsEntitiesList.get(getAdapterPosition());
                listItemClickListener.onListItemClick(movieTvShowEntity.getMovieTvShowId());

            } else if (moviesTvShowsEntitiesList == null) {
                MovieTvShowsDetails movieTvShowsDetails = moviesTvShowsDetailsList.get(getAdapterPosition());
                listItemClickListener.onListItemClick(movieTvShowsDetails.getmId());
            }
        }
    }


}
