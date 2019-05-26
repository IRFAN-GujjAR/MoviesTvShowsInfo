package com.example.moviestvshowsinfo;

public class MovieTvShowsDetails {

    private int mId;
    private String mName;
    private String mGenres;
    private String mLanguage;
    private String mReleaseDate;
    private double mAverageVote;
    private boolean mAdult;
    private String mPosterPath;
    private String mBackgroundPath;

    private String mOverView;
    private int mBudget;
    private int mRevenue;
    private String mProductionCompanies;


    public MovieTvShowsDetails(int mId, String mName, String mGenres, String mLanguage, String mReleaseDate, double mAverageVote, boolean mAdult,
                               String mPosterPath, String mBackgroundPath) {
        this.mId = mId;
        this.mName = mName;
        this.mGenres = mGenres;
        this.mLanguage = mLanguage;
        this.mReleaseDate = mReleaseDate;
        this.mAverageVote = mAverageVote;
        this.mAdult = mAdult;
        this.mPosterPath = mPosterPath;
        this.mBackgroundPath = mBackgroundPath;
    }

    public MovieTvShowsDetails(int mId, String mName, String mGenres, String mLanguage, String mReleaseDate, double mAverageVote, boolean mAdult,
                               String mPosterPath, String mBackgroundPath, String mOverView, int mBudget, int mRevenue, String mProductionCompanies) {
        this.mId = mId;
        this.mName = mName;
        this.mGenres = mGenres;
        this.mLanguage = mLanguage;
        this.mReleaseDate = mReleaseDate;
        this.mAverageVote = mAverageVote;
        this.mAdult = mAdult;
        this.mPosterPath = mPosterPath;
        this.mBackgroundPath=mBackgroundPath;
        this.mOverView = mOverView;
        this.mBudget = mBudget;
        this.mRevenue = mRevenue;
        this.mProductionCompanies = mProductionCompanies;
    }


    public int getmId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }

    public String getmGenres() {
        return mGenres;
    }

    public String getmLanguage() {
        return mLanguage;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public double getmAverageVote() {
        return mAverageVote;
    }

    public boolean ismAdult() {
        return mAdult;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public String getmBackgroundPath() {
        return mBackgroundPath;
    }

    public String getmOverView() {
        return mOverView;
    }

    public int getmBudget() {
        return mBudget;
    }

    public int getmRevenue() {
        return mRevenue;
    }

    public String getmProductionCompanies() {
        return mProductionCompanies;
    }
}

