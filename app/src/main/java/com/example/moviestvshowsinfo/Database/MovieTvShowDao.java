package com.example.moviestvshowsinfo.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieTvShowDao {

    @Query("SELECT * FROM MoviesTVShows WHERE tvShowName IS NULL")
    LiveData<List<MovieTvShowEntity>> loadAllMovies();

    @Query("SELECT * FROM MoviesTVShows WHERE movieTitle IS NULL")
    LiveData<List<MovieTvShowEntity>> loadAllTvShows();

    @Query("SELECT * FROM MoviesTVShows WHERE movieTvShowId=:id")
    LiveData<MovieTvShowEntity> loadSignleMovieOrTvShow(int id);

    @Query("SELECT movieTvShowId FROM MoviesTVShows WHERE movieTvShowId=:id")
    int loadId(int id);

    @Query("SELECT posterImagePath FROM MoviesTVShows WHERE movieTvShowId=:id")
    String loadPosterImage(int id);

    @Query("SELECT backgroundImagePath FROM MoviesTVShows WHERE movieTvShowId=:id")
    String loadBackgroundImage(int id);

    @Query("SELECT youtubeKey FROM MoviesTVShows WHERE movieTvShowId=:id")
    String getYoutubeKey(int id);

    @Query("DELETE FROM MoviesTVShows WHERE movieTvShowId=:id")
    int deleteSingleMovieOrTvShow(int id);

    @Insert
    void insertMovieTvShow(MovieTvShowEntity movieTvShowEntity);

    @Delete
    void deleteMovieTvShow(MovieTvShowEntity movieTvShowEntity);
}
