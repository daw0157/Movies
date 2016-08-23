package example.com.movies;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 8/22/2016.
 */
public class GridAdapter extends BaseAdapter {

    private final String LOG_TAG = "GRID_ADAPTER";
    private ArrayList<Movie> movies = new ArrayList<Movie>();
    private Context mContext;

    public GridAdapter(Context context){
        mContext = context;
        movies = new ArrayList<Movie>();
    }

    public void setMovies(ArrayList<Movie> movies){
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return movies.get(i).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_item, null);
            //TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
            //textView.setText(movies.get(position).title);
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + movies.get(position).posterPath).into(imageView);
            Log.v(LOG_TAG, "getView called");
        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}
