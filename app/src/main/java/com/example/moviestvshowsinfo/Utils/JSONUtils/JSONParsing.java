package com.example.moviestvshowsinfo.Utils.JSONUtils;

import android.text.TextUtils;

import com.example.moviestvshowsinfo.MovieTvShowsDetails;
import com.example.moviestvshowsinfo.R;
import com.example.moviestvshowsinfo.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class JSONParsing {

    private static URL createUrl(String string) {
        URL url = null;
        try {
            url = new URL(string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String makeHttpRequest(URL url) {

        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {

                inputStream = httpURLConnection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\A");

                while (scanner.hasNext()) {
                    jsonResponse = scanner.next();
                }
                scanner.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonResponse;
    }

    private static List<MovieTvShowsDetails> extractDataFromJsonResponse(String jsonResponse) {
        List<MovieTvShowsDetails> list = new ArrayList<>();
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        try {

            JSONObject jsonObject = new JSONObject(jsonResponse);

            JSONArray results = jsonObject.optJSONArray("results");
            if (results != null&&results.length()!=0) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject currentMovie = results.optJSONObject(i);

                    int movieTvShowId = currentMovie.optInt("id");

                    String title = currentMovie.optString("title");
                    if (title == null || title.isEmpty()) {
                        title = currentMovie.optString("name");
                        if (title == null || title.isEmpty()) {
                            title = "";
                        }
                    }

                    String language = currentMovie.optString("original_language");

                    if (language == null || language.isEmpty()) {
                        language = "";
                    }

                    String releaseDate = currentMovie.optString("release_date");

                    if (releaseDate == null || releaseDate.isEmpty()) {
                        releaseDate = "";
                    }

                    if (releaseDate == null || releaseDate.isEmpty()) {
                        releaseDate = currentMovie.optString("first_air_date");
                        if (releaseDate == null || releaseDate.isEmpty()) {
                            releaseDate = "";
                        }
                    }

                    if(releaseDate!=null&&!releaseDate.isEmpty()){
                        String year=releaseDate.substring(2,4);
                        String month=releaseDate.substring(5,7);
                        String day=releaseDate.substring(8,10);
                        releaseDate=day+"-"+month+"-"+year;
                    }

                    boolean adult = false;
                    adult = currentMovie.optBoolean("adult");

                    double averageVote = currentMovie.optDouble("vote_average");
                    if (averageVote < 1) {
                        averageVote = 0;
                    }

                    String posterPath = "";
                    String backgroundPath = "";

                    if (!currentMovie.isNull("poster_path")) {
                        posterPath = currentMovie.optString("poster_path");
                        posterPath = "https://image.tmdb.org/t/p/original" + posterPath;

                    }
                    if (!currentMovie.isNull("backdrop_path")) {
                        backgroundPath = currentMovie.optString("backdrop_path");
                        backgroundPath = "https://image.tmdb.org/t/p/original" + backgroundPath;
                    }


                    JSONArray genreIds = currentMovie.optJSONArray("genre_ids");
                    String genres = "";
                    if (genreIds != null) {

                        int[] genresIntegerArray = Utils.getMainContext().getResources().getIntArray(R.array.movies_tv_shows_integer_genres_array);
                        String[] genresStringArray = Utils.getMainContext().getResources().getStringArray(R.array.movies_tv_shows_string_genres_array);

                        for (int j = 0; j < genreIds.length(); j++) {
                            int id = genreIds.optInt(j);

                            for (int k = 0; k < genresIntegerArray.length; k++) {
                                if (id == genresIntegerArray[k]) {
                                    genres = genres + genresStringArray[k] + ", ";
                                }
                            }
                        }
                        if (genres != null && !genres.isEmpty()) {
                            genres = genres.substring(0, genres.length() - 2);
                        }

                    }
                    MovieTvShowsDetails currentMovieTvShowsDetails = new MovieTvShowsDetails(movieTvShowId, title, genres, language, releaseDate,
                            averageVote, adult, posterPath, backgroundPath);

                    list.add(currentMovieTvShowsDetails);

                }
            }else {
                list=null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static MovieTvShowsDetails extractDataFromJsonResponesForDetails(String jsonResponse) {

        MovieTvShowsDetails movieTvShowsDetails = null;

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }


        try {

            JSONObject jsonObject = new JSONObject(jsonResponse);

            int movieTvShowId = jsonObject.optInt("id");

            String title = jsonObject.optString("title");
            if (title == null || title.isEmpty()) {
                title = jsonObject.optString("name");
                if (title == null || title.isEmpty()) {
                    title = "";
                }
            }

            String genres = "";
            JSONArray genresArray = jsonObject.optJSONArray("genres");
            if (genresArray != null) {
                for (int i = 0; i < genresArray.length(); i++) {
                    JSONObject currentGenre = genresArray.getJSONObject(i);
                    String genreName = currentGenre.optString("name");
                    genres = genres + genreName + ", ";
                }
                if (genres != null && !genres.isEmpty()) {
                    genres = genres.substring(0, genres.length() - 2);
                }
            }

            String language = jsonObject.optString("original_language");
            if (language == null || language.isEmpty()) {
                language = "";
            }

            String releaseDate = jsonObject.optString("release_date");
            if (releaseDate == null || releaseDate.isEmpty()) {
                releaseDate = jsonObject.optString("first_air_date");
                if (releaseDate == null || releaseDate.isEmpty()) {
                    releaseDate = "";
                }
            }

            if(releaseDate!=null&&!releaseDate.isEmpty()){
                String year=releaseDate.substring(2,4);
                String month=releaseDate.substring(5,7);
                String day=releaseDate.substring(8,10);
                releaseDate=day+"-"+month+"-"+year;
            }

            double averageVote = jsonObject.optDouble("vote_average");
            if (averageVote < 1) {
                averageVote = 0;
            }

            boolean adult = false;
            adult = jsonObject.optBoolean("adult");


            String posterPath = "";
            String backgroundPath = "";

            if (!jsonObject.isNull("poster_path")) {
                posterPath = jsonObject.optString("poster_path");
                posterPath = "https://image.tmdb.org/t/p/original" + posterPath;

            }
            if (!jsonObject.isNull("backdrop_path")) {
                backgroundPath = jsonObject.optString("backdrop_path");
                backgroundPath = "https://image.tmdb.org/t/p/original" + backgroundPath;
            }


            String overView = jsonObject.optString("overview");
            if (overView == null || overView.isEmpty()) {
                overView = "";
            }

            int budget = jsonObject.optInt("budget");
            if (budget < 1) {
                budget = 0;
            }

            int revenue = jsonObject.optInt("revenue");
            if (revenue < 1) {
                revenue = 0;
            }

            String productionCompanies = "";
            JSONArray productionComapniesArray = jsonObject.optJSONArray("production_companies");
            if (productionComapniesArray != null) {
                for (int i = 0; i < productionComapniesArray.length(); i++) {
                    JSONObject currentComapny = productionComapniesArray.optJSONObject(i);
                    String companyName = currentComapny.optString("name");
                    productionCompanies = productionCompanies + companyName + "\n";
                }
            }

            movieTvShowsDetails = new MovieTvShowsDetails(movieTvShowId, title, genres, language, releaseDate, averageVote, adult, posterPath,
                    backgroundPath,overView, budget, revenue, productionCompanies);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieTvShowsDetails;
    }

    private static String extractYoutubeVideoKeyFromJsonResponse(String jsonResponse) {
        String youTubeVideoKey = "";

        if (jsonResponse == null || jsonResponse.isEmpty()) {
            return null;
        }

        try {

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray resultsArray = jsonObject.optJSONArray("results");
            if (resultsArray != null) {
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject currentResult = resultsArray.optJSONObject(i);
                    String type = currentResult.optString("type");
                    if (type.equals("Trailer")) {
                        youTubeVideoKey = currentResult.optString("key");
                        break;
                    } else if (type.equals("Teaser")) {
                        youTubeVideoKey = currentResult.optString("key");
                    }
                }
            }

        } catch (
                JSONException e) {
            e.printStackTrace();
        }

        return youTubeVideoKey;
    }

    /**
     *{@fetchDataForList} loads the list of Movies or Tv Shows.
     */
    public static List<MovieTvShowsDetails> fetchDataForList(String stringUrl) {
        URL url = createUrl(stringUrl);
        String jsonResponse = makeHttpRequest(url);
        List<MovieTvShowsDetails> list = extractDataFromJsonResponse(jsonResponse);
        return list;
    }

    /**
     *{@fetchDataForDetails} loads the details of specific Movie or Tv Show.
     */
    public static MovieTvShowsDetails fetchDataForDetails(String stringUrl) {
        URL url = createUrl(stringUrl);
        String jsonResponse = makeHttpRequest(url);
        MovieTvShowsDetails movieTvShowsDetails = extractDataFromJsonResponesForDetails(jsonResponse);
        return movieTvShowsDetails;
    }

    /**
     *{@fetchYoutubeVideoKey} loads the Youtube Video Key of specific Movie or Tv Show.
     */
    public static String fetchYoutubeVideoKey(String stringUrl) {
        URL url = createUrl(stringUrl);
        String jsonResponse = makeHttpRequest(url);
        String youTubeVideoKey = extractYoutubeVideoKeyFromJsonResponse(jsonResponse);
        return youTubeVideoKey;
    }

}
