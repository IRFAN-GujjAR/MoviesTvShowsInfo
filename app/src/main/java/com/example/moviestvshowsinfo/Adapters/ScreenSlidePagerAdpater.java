package com.example.moviestvshowsinfo.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.example.moviestvshowsinfo.Fragment.EmptyFragment;
import com.example.moviestvshowsinfo.Fragment.MovieTvShowsFragment;
import com.example.moviestvshowsinfo.Utils.Utils;

public class ScreenSlidePagerAdpater extends FragmentStatePagerAdapter {

    /**
     * Defining the number of pages which is 2:
     * 1-Movies
     * 2-Tv Shows
     */
    private static final int NUMBER_OF_PAGES = 2;

    private int number = 0;

    public ScreenSlidePagerAdpater(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int i) {


        if (number == 0) {
            if (Utils.isSearched) {
                switch (i) {
                    case 0:
                        Utils.setCurrentUrl(Utils.SEARCH_MOVIES_PRIMARY_URL + Utils.getSearchedQuery());
                        break;
                    case 1:
                        Utils.setCurrentUrl(Utils.SEARCH_TV_SHOWS_PRIMARY_URL + Utils.getSearchedQuery());
                        break;
                }
            }
        }
        number++;

        /**
         * Returning empty fragment if MovieTvShow List is empty.
         */
        if (Utils.isEmptyMovieTvShowList()) {
            return new EmptyFragment();
        }

        return new MovieTvShowsFragment();

    }

    @Override
    public int getCount() {
        return NUMBER_OF_PAGES;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                return title = "MOVIES";
            case 1:
                return title = "TV SHOWS";
        }
        return title;
    }


    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }


    /**
     * This is the method which will be called to refresh the adapter data.
     */
    public void notifyDataChanged() {
        number = 0;
        notifyDataSetChanged();

    }
}
