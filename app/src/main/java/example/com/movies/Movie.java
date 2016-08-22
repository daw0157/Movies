package example.com.movies;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dan on 8/22/2016.
 */
public class Movie {
    public String posterPath;
    public boolean adult;
    public String overview;
    public String release_date;
    public List<Integer> genre_ids = new ArrayList<>();
    public int id;
    public String original_title;
    public String original_language;
    public String title;
    public String backdropPath;
    public double popularity;
    public int voteCount;
    public boolean video;
    public double voteAverage;
}
