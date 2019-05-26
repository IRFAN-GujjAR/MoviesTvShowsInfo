package com.example.moviestvshowsinfo;

import android.content.SearchRecentSuggestionsProvider;

/**
 * This class provides search suggestions in the search menu.
 */
public class SearchSuggestionsProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY="com.example.moviestvshowsinfo.SearchSuggestionsProvider";
    public final static int MODE=DATABASE_MODE_QUERIES;

    public SearchSuggestionsProvider(){
        setupSuggestions(AUTHORITY,MODE);
    }
}
