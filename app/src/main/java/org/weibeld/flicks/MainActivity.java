package org.weibeld.flicks;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.thedeanda.lorem.LoremIpsum;

import org.weibeld.flicks.api.ApiResponseMovieList;
import org.weibeld.flicks.api.ApiService;
import org.weibeld.flicks.databinding.ActivityMainBinding;
import org.weibeld.flicks.databinding.ItemMovieBinding;
import org.weibeld.flicks.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static org.weibeld.flicks.api.ApiResponseMovieList.Movie;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    MainActivity mActivity;
    Retrofit mRetrofit;
    ListView mListView;
    ArrayAdapter<Movie> mAdapter;
    List<Movie> mMovies;
    SwipeRefreshLayout mSwipeRefresh;
    ActivityMainBinding b;

    // Variables used to generate dummy movie content
    Random mRand;
    LoremIpsum mLorem;
    // Dummy movie titles (from http://www.seventhsanctum.com/generate.php?Genname=bmovie)
    final String[] DUMMY_TITLES = {
            "Beyond Science",
            "Child of Horror, Part I",
            "Destiny of The Incredible Bees",
            "Dreadful House",
            "Evil Odyssey to Jupiter",
            "Mission: Chaos",
            "Monsters of Passion",
            "Offspring of Justice",
            "Passion Pit",
            "Son of Chaos",
            "The Death Breed",
            "The Decadent Indestructible Man",
            "The Eaters from Hell",
            "The Luscious Destiny of the SS",
            "The Mind Ravagers",
            "The Radioactive Harvesters",
            "The Secret of The Disease of Africa",
            "The Shark-People Unbound",
            "War of Horror Mansion",
            "War of the Centaur",
            "Breed Battle",
            "Expedition to Venus",
            "Guinnevere in Saint Louis",
            "Heart of the Dragon",
            "I was a teenaged Satan",
            "King Sensuality",
            "Love Healers",
            "Offspring of The Licentious Bird-Women",
            "Terror Canyon, The Final Chapter",
            "The Alpha Women",
            "The Atomic Dragons",
            "The Bloodthirsty Curse of the SS",
            "The Madness Beast, Chapter IV",
            "The Pestilence Beasts",
            "The Rage of Tom Thumb",
            "The Sensational Secret of The Love of Chicago",
            "The Tales of Mu",
            "The Terrible Case of Gloria Roberts",
            "Walter Turner Lives",
            "Centaur Battle 2005",
            "Clone Assault!",
            "Curse of The Atomic Women, The Origin",
            "Death Circus",
            "Donna Miller Forever",
            "Holiness Mansion",
            "Innocents of Suffering",
            "Murderous Monday",
            "My Mom Married Jesse James",
            "Offspring of Captain Nemo",
            "Pilgramage to The Moon",
            "Sensation Feast",
            "Siege of The Damnation Creature, Part IX",
            "The Diseased Biker",
            "The Fascination of America, A New Beginning",
            "The Horrible Mystery of The Terrible Scorpion-People",
            "The Kiss of Napoleon",
            "The Love of Santa Claus",
            "The Piranha-Person in New York",
            "War of The Devil",
            "Attack of the Gigantic Ants",
            "Brood of Disease",
            "Festival of Lizzie Borden",
            "Golden Victory",
            "Hate Innocent",
            "I Married Lizzie Borden",
            "Invasion of the Children of Evil",
            "Love Beach",
            "Passion Controller",
            "Professor Wonder",
            "The Case of the SS",
            "The Immortal Women",
            "The Inhuman Men",
            "The Legendary Alicia Davis",
            "The Policewoman Broken",
            "The Tales of the Gorillas",
            "The Touch of Hitler",
            "Wednesday of Love",
            "Brood of Holiness",
            "Creature of Passion",
            "Depraved Mansion",
            "Fate of The Atomic Kid",
            "Kiss of Insanity",
            "Lizzie Borden Abides",
            "Love of Disease",
            "Mudslide",
            "Plague Nun",
            "Professor Death",
            "The Body Harvester",
            "The Leech-Men from Heaven",
            "The Leech-Person Unbound",
            "The Magic of De Sade",
            "The Mindless Alien",
            "The Policeman from Beyond",
            "The Soul of Xavier Anderson",
            "The Touch of Frankenstein"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(b.toolbar);

        // Initialise member variables
        mActivity = this;
        mRetrofit = Util.setupRetrofit();
        mListView = b.lvMovies;
        mMovies = new ArrayList<>();
        mAdapter = new MovieAdapter(this, (ArrayList<Movie>) mMovies);
        mListView.setAdapter(mAdapter);

        // Initialise variables used ot generate dummy movie content
        mLorem = LoremIpsum.getInstance();
        mRand = new Random();

        // Set up "pull to refresh"
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        // Setup refresh listener which triggers new data loading
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                mAdapter.clear();
                getNowPlaying();
            }
        });
        // Configure the refreshing colors
        mSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Start DetailActiviry and put
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra(getString(R.string.intent_extra_movie), mMovies.get(position));
                startActivity(intent);
            }
        });

        getNowPlaying();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_dummy:
                return true;
        }
        return false;
    }

    private void getNowPlaying() {
        ApiService api = mRetrofit.create(ApiService.class);
        Call<ApiResponseMovieList> call = api.apiGetNowPlaying();
        call.enqueue(new Callback<ApiResponseMovieList>() {
            @Override
            public void onResponse(Call<ApiResponseMovieList> call, Response<ApiResponseMovieList> response) {
                int statusCode = response.code();
                ApiResponseMovieList body = response.body();
                List<Movie> movies = body.results;

                // Replace images, titles, and descriptions of the movies with dummy content
                for (Movie movie : movies) {
                    movie.posterPath = "https://unsplash.it/342/513?image=" + mRand.nextInt(1000);
                    movie.backdropPath = "https://unsplash.it/780/439?image=" + mRand.nextInt(1000);
                    movie.title = DUMMY_TITLES[mRand.nextInt(DUMMY_TITLES.length)];
                    movie.overview = mLorem.getParagraphs(1, 1);
                }

                mAdapter.addAll(movies);
                mSwipeRefresh.setRefreshing(false);  // Remove the spinning referesh item
            }

            @Override
            public void onFailure(Call<ApiResponseMovieList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    public class MovieAdapter extends ArrayAdapter<Movie> {

        private final String LOG_TAG = MovieAdapter.class.getSimpleName();

        public MovieAdapter(Context context, ArrayList<Movie> items) {
            super(context, 0, items);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Movie movie = getItem(position);
            ItemMovieBinding binding;

            if (convertView == null) {
                binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_movie, parent, false);
                convertView = binding.getRoot();
                convertView.setTag(binding);
            }
            else
                binding = (ItemMovieBinding) convertView.getTag();

            binding.tvTitle.setText(movie.title);
            binding.tvOverview.setText(movie.overview);

            // Load the dummy movie poster or backdrop into the ImageView
            if (Util.isPortrait(mActivity))
                Glide.with(mActivity)
                        .load(movie.posterPath)
                        .placeholder(R.drawable.poster_placeholder_185x278)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.ivImage);
            else
                Glide.with(mActivity)
                        .load(movie.backdropPath)
                        .placeholder(R.drawable.backdrop_placeholder_780x439)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.ivImage);

//            if (Util.isPortrait(mActivity))
//                Util.loadImage(mActivity, Util.TYPE_POSTER, ApiService.POSTER_SIZE_W185, movie.posterPath, binding.ivImage);
//            else
//                Util.loadImage(mActivity, Util.TYPE_BACKDROP, ApiService.BACKDROP_SIZE_W780, movie.backdropPath, binding.ivImage);

            return convertView;
        }
    }
}
