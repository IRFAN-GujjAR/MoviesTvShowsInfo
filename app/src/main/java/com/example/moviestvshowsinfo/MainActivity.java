package com.example.moviestvshowsinfo;


import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.moviestvshowsinfo.Utils.Utils;
import com.example.moviestvshowsinfo.Adapters.ScreenSlidePagerAdpater;
import com.example.moviestvshowsinfo.databinding.ActivityMainBinding;

/**
 * This is the first activity which will be executed on opening the app.
 */

public class MainActivity extends AppCompatActivity {

    /**
     * Declaring the Data Binding instance instead of declaring number of instances for multiple views.
     */
    private ActivityMainBinding activityMainBinding;


    /**
     * Declaring the Search View which will be used for search menu item.
     */
    private SearchView searchView;

    /**
     * Declaring the Search Menu Item and Clear Search Menu Item so that we can disable their visibility in favourites movies or tv shows.
     */
    private MenuItem searchMenuItem;
    private MenuItem clearSearchHistoryMenuItem;

    /**
     * This variable is declared to handle the logic of {@OnNewIntent).
     */
    private Boolean isNewIntent = true;


    private ScreenSlidePagerAdpater pagerAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        /**
         * Setting the context of MainActivity in Utils class, so we can use it in the whole app
         * wherever we need it.
         */
        Utils.setMainContext(MainActivity.this);


        setSupportActionBar(activityMainBinding.toolbar);

        /**
         * Disabling the swiping of viewpager.
         */
        activityMainBinding.viewPager.enableSwiping(false);

        pagerAdapter = new ScreenSlidePagerAdpater(getSupportFragmentManager());

        /**
         * Making the {@pagerAdapter} available to all the classes and fragments in app where we can
         * notify the adapter for any change.
         */
        Utils.screenSlidePagerAdpater = pagerAdapter;

        activityMainBinding.viewPager.setAdapter(pagerAdapter);

        activityMainBinding.tabLayout.setupWithViewPager(activityMainBinding.viewPager);


        activityMainBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                Utils.setFavouriteTvShows(false);
                Utils.setFavouriteMovies(false);
                /**
                 * As in the there are two Tab Options:
                 * 1-Movies  > Case 0
                 * 2-Tv Shows > Case 1
                 * So both have different bottomNavigation View,so for this bottomNavigation View is cleared
                 * and re-inflated in both cases.
                 */
                switch (i) {
                    case 0:
                        activityMainBinding.bottomNavigation.getMenu().clear();
                        activityMainBinding.bottomNavigation.inflateMenu(R.menu.movies_bottom_navigation_menu);
                        activityMainBinding.bottomNavigation.setSelectedItemId(R.id.movies_most_popular_menu_item);
                        Utils.setMovie(true);
                        Utils.setTvShow(false);
                        Utils.setCurrentUrl(Utils.MOVIES_POPULAR_URL);
                        pagerAdapter.notifyDataChanged();
                        break;
                    case 1:
                        activityMainBinding.bottomNavigation.getMenu().clear();
                        activityMainBinding.bottomNavigation.inflateMenu(R.menu.tv_shows_bottom_navigation_menu);
                        activityMainBinding.bottomNavigation.setSelectedItemId(R.id.tv_shows_popular_menu_item);
                        Utils.setTvShow(true);
                        Utils.setMovie(false);
                        Utils.setCurrentUrl(Utils.TV_SHOWS_POPULAR_URL);
                        pagerAdapter.notifyDataChanged();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        /**
         * If the app is running on Tablet greater than Screen Size 9.5 then
         * bottomNavigationView textAppearance will be changed
         */

        if (Utils.getDisplayMetrics(this) >= 9.5) {
            activityMainBinding.bottomNavigation.setItemTextAppearanceActive(R.style.BottomNavigationViewForTablet);
            activityMainBinding.bottomNavigation.setItemTextAppearanceInactive(R.style.BottomNavigationViewForTablet);
        }


        /**
         * Setting the initial selection of bottomNavigation.
         * Setting the Default or Initial Url for Loading movies.
         */
        activityMainBinding.bottomNavigation.setSelectedItemId(R.id.movies_most_popular_menu_item);
        Utils.setCurrentUrl(Utils.MOVIES_POPULAR_URL);
        Utils.setMovie(true);
        Utils.setTvShow(false);
        Utils.setFavouriteMovies(false);
        Utils.setFavouriteTvShows(false);
        Utils.setEmptyMovieTvShowList(false);
        activityMainBinding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.movies_most_popular_menu_item:
                        Utils.setEmptyMovieTvShowList(false);
                        Utils.setCurrentUrl(Utils.MOVIES_POPULAR_URL);
                        Utils.setFavouriteMovies(false);
                        Utils.setFavouriteTvShows(false);
                        searchMenuItem.setVisible(true);
                        clearSearchHistoryMenuItem.setVisible(true);
                        pagerAdapter.notifyDataChanged();
                        break;
                    case R.id.movies_top_rated_menu_item:
                        Utils.setEmptyMovieTvShowList(false);
                        Utils.setCurrentUrl(Utils.MOVIES_TOP_RATED_URL);
                        Utils.setFavouriteMovies(false);
                        Utils.setFavouriteTvShows(false);
                        searchMenuItem.setVisible(true);
                        clearSearchHistoryMenuItem.setVisible(true);
                        pagerAdapter.notifyDataChanged();
                        break;
                    case R.id.movies_latest_menu_item:
                        Utils.setEmptyMovieTvShowList(false);
                        Utils.setCurrentUrl(Utils.MOVIES_NOW_PLAYING_URL);
                        Utils.setFavouriteMovies(false);
                        Utils.setFavouriteTvShows(false);
                        searchMenuItem.setVisible(true);
                        clearSearchHistoryMenuItem.setVisible(true);
                        pagerAdapter.notifyDataChanged();
                        break;
                    case R.id.movies_upcoming_menu_item:
                        Utils.setEmptyMovieTvShowList(false);
                        Utils.setCurrentUrl(Utils.MOVIES_UPCOMING_URL);
                        Utils.setFavouriteMovies(false);
                        Utils.setFavouriteTvShows(false);
                        pagerAdapter.notifyDataChanged();
                        clearSearchHistoryMenuItem.setVisible(true);
                        searchMenuItem.setVisible(true);
                        break;
                    case R.id.tv_shows_popular_menu_item:
                        Utils.setEmptyMovieTvShowList(false);
                        Utils.setCurrentUrl(Utils.TV_SHOWS_POPULAR_URL);
                        Utils.setFavouriteMovies(false);
                        Utils.setFavouriteTvShows(false);
                        searchMenuItem.setVisible(true);
                        clearSearchHistoryMenuItem.setVisible(true);
                        pagerAdapter.notifyDataChanged();
                        break;
                    case R.id.tv_shows_top_rated_menu_item:
                        Utils.setEmptyMovieTvShowList(false);
                        Utils.setCurrentUrl(Utils.TV_SHOWS_TOP_RATED_URL);
                        Utils.setFavouriteMovies(false);
                        Utils.setFavouriteTvShows(false);
                        searchMenuItem.setVisible(true);
                        clearSearchHistoryMenuItem.setVisible(true);
                        pagerAdapter.notifyDataChanged();
                        break;
                    case R.id.tv_shows_airing_today_menu_item:
                        Utils.setEmptyMovieTvShowList(false);
                        Utils.setCurrentUrl(Utils.TV_SHOWS_AIRING_TODAY_URL);
                        Utils.setFavouriteMovies(false);
                        Utils.setFavouriteTvShows(false);
                        searchMenuItem.setVisible(true);
                        clearSearchHistoryMenuItem.setVisible(true);
                        pagerAdapter.notifyDataChanged();
                        break;
                    case R.id.tv_shows_on_the_air_menu_item:
                        Utils.setEmptyMovieTvShowList(false);
                        Utils.setCurrentUrl(Utils.TV_SHOWS_ON_THE_AIR_URL);
                        Utils.setFavouriteMovies(false);
                        Utils.setFavouriteTvShows(false);
                        searchMenuItem.setVisible(true);
                        clearSearchHistoryMenuItem.setVisible(true);
                        pagerAdapter.notifyDataChanged();
                        break;
                    case R.id.movies_favourite_menu_item:
                        Utils.setEmptyMovieTvShowList(false);
                        Utils.setFavouriteMovies(true);
                        Utils.setFavouriteTvShows(false);
                        Utils.setIsSearched(false);
                        searchMenuItem.setVisible(false);
                        clearSearchHistoryMenuItem.setVisible(false);
                        pagerAdapter.notifyDataChanged();
                        break;
                    case R.id.tv_favourite_menu_item:
                        Utils.setEmptyMovieTvShowList(false);
                        Utils.setFavouriteTvShows(true);
                        Utils.setFavouriteMovies(false);
                        Utils.setIsSearched(false);
                        searchMenuItem.setVisible(false);
                        clearSearchHistoryMenuItem.setVisible(false);
                        pagerAdapter.notifyDataChanged();
                        break;
                }
                return true;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        /**
         * Creating menuItem from search_menu_item id and saving it into searchView instance
         * so, it can be used later for saving submitting query and saving search suggestions.
         */

        searchMenuItem = menu.findItem(R.id.search_menu_item);
        clearSearchHistoryMenuItem=menu.findItem(R.id.delete_search_history);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Utils.setIsSearched(false);
                activityMainBinding.cardViewBottomNavigationView.setVisibility(View.VISIBLE);
                if (Utils.isMovie()) {
                    activityMainBinding.bottomNavigation.setSelectedItemId(R.id.movies_most_popular_menu_item);
                    Utils.setCurrentUrl(Utils.MOVIES_POPULAR_URL);
                    pagerAdapter.notifyDataChanged();
                } else if (Utils.isTvShow()) {
                    activityMainBinding.bottomNavigation.setSelectedItemId(R.id.tv_shows_popular_menu_item);
                    Utils.setCurrentUrl(Utils.TV_SHOWS_POPULAR_URL);
                    pagerAdapter.notifyDataChanged();
                }

                return true;
            }
        });


        return true;
    }


    /**
     * This method will be executed when user will submit the query and it will execute atleast twice,
     * so that's whe {@isNewIntent} boolean variable is created so it helps to check whether is {@onNewIntent}
     * is executed first time or second time, if it is first time then get search and submit query and save search suggestions
     * otherwise it will not execute.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (isNewIntent) {
            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                String query = intent.getStringExtra(SearchManager.QUERY);
                searchView.setQuery(query, true);
                searchView.clearFocus();
                saveRecentSearchSuggestions(query);
                Utils.setIsSearched(true);
                Utils.setSearchedQuery(query);
                Utils.setEmptyMovieTvShowList(false);
                activityMainBinding.cardViewBottomNavigationView.setVisibility(View.GONE);
                pagerAdapter.notifyDataChanged();
                isNewIntent = false;
            }
        } else {
            isNewIntent = true;
        }
    }

    private void saveRecentSearchSuggestions(String query) {

        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
                this,
                SearchSuggestionsProvider.AUTHORITY,
                SearchSuggestionsProvider.MODE);
        suggestions.saveRecentQuery(query, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_search_history:
                alertDialogForClearingSearchHistory();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method to show alert dialog whenever user presses the clear search history menu button
     * and if user presses "Yes" then delete search history otherwise dismiss the dialog.
     */
    private void alertDialogForClearingSearchHistory() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_dialog_search_history_message)
                .setPositiveButton(R.string.alert_dialog_search_history_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(MainActivity.this,
                                SearchSuggestionsProvider.AUTHORITY,
                                SearchSuggestionsProvider.MODE);
                        suggestions.clearHistory();
                    }
                })
                .setNegativeButton(R.string.alert_dialog_search_history_negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
