package example.com.movies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String API_KEY = "8acc697ba44e770f378dfda2c109c170";
    public GridAdapter adapter = null;
    private HttpURLConnection urlConnection = null;
    private BufferedReader reader = null;
    private String jsonStr = "";
    private ArrayList<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getMovies();

        GridView gridView = (GridView)findViewById(R.id.main_grid_view);
        adapter = new GridAdapter(this);
        gridView.setAdapter(adapter);
    }

    public void getMovies(){
        GetPopularMoviesTask moviesTask = new GetPopularMoviesTask();
        moviesTask.execute();
    }

    public class GetPopularMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = GetPopularMoviesTask.class.getSimpleName();

        protected List<Movie> doInBackground(String... params){
            String[] forecasts = null;
            // url:  http://api.openweather.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7
            try{
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath("popular")
                        .appendQueryParameter("api_key", API_KEY);
                URL url = new URL(builder.build().toString());

                //create url connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //read response
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null){
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }

                if(buffer.length() == 0){
                    return null;
                }
                jsonStr = buffer.toString();
            } catch(Exception ex){
                Log.e("Fragment", "Error", ex);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Fragment", "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(jsonStr);
            } catch (Exception e){
                Log.e(LOG_TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> result) {
            if(result != null){
                adapter.setMovies(mMovies);
                adapter.notifyDataSetChanged();
            }
        }

        private List<Movie> getMovieDataFromJson(String movieJsonStr)
                throws JSONException {

            ArrayList<Movie> movies = new ArrayList<Movie>();

            // These are the names of the JSON objects that need to be extracted.
            final String RESULTS = "results";
            final String POSTER_PATH = "poster_path";
            final String ADULT = "adult";
            final String OVERVIEW = "overview";
            final String RELEASE_DATE = "release_date";
            final String GENRE_IDS = "genre_ids";
            final String ID = "id";
            final String ORIGINAL_TITLE = "original_title";
            final String ORIGINAL_LANGUAGE = "original_language";
            final String TITLE = "title";
            final String BACKDROP_PATH = "backdrop_path";
            final String POPULARITY = "popularity";
            final String VOTE_COUNT = "vote_count";
            final String VIDEO = "video";
            final String VOTE_AVERAGE = "vote_average";

            JSONObject resultJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = resultJson.getJSONArray(RESULTS);

            for(int i = 0; i < movieArray.length(); i++){
                Movie mov = new Movie();
                JSONObject movie = movieArray.getJSONObject(i);

                mov.posterPath = movie.getString(POSTER_PATH);
                mov.adult = movie.getBoolean(ADULT);
                mov.overview = movie.getString(OVERVIEW);
                mov.release_date = movie.getString(RELEASE_DATE);
                mov.id = movie.getInt(ID);
                mov.original_title = movie.getString(ORIGINAL_TITLE);
                mov.original_language = movie.getString(ORIGINAL_LANGUAGE);
                mov.title = movie.getString(TITLE);
                mov.backdropPath = movie.getString(BACKDROP_PATH);
                mov.popularity = movie.getDouble(POPULARITY);
                mov.voteCount = movie.getInt(VOTE_COUNT);
                mov.video = movie.getBoolean(VIDEO);
                mov.voteAverage = movie.getDouble(VOTE_AVERAGE);

                JSONArray genres = movie.getJSONArray(GENRE_IDS);
                for(int j = 0; j < genres.length(); j++){
                    mov.genre_ids.add(genres.getInt(j));
                }

                movies.add(mov);
            }
            mMovies = movies;
            return movies;

        }
    }
}
