package fr.hedwin.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.hedwin.R
import fr.hedwin.tmdb.`object`.Credits
import fr.hedwin.tmdb.`object`.DbMovie
import fr.hedwin.tmdb.`object`.Genre
import fr.hedwin.tmdb.retrofit.TMDB.FR
import fr.hedwin.tmdb.retrofit.TMDB.SERVICE
import fr.hedwin.ui.dashboard.DashboardFragment
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        // Retrieve the data passed from the MainActivity
        val movie = ObjectMapper().readValue(intent.getStringExtra("movie"), object : TypeReference<DbMovie?>() {})

        if(movie != null){
            title = "Détail du film : "+ movie.title;

            val textView = findViewById<TextView>(R.id.titleView);
            val postView = findViewById<ImageView>(R.id.posterView);
            val dateView = findViewById<TextView>(R.id.dateView);
            val actorView = findViewById<TextView>(R.id.actorView);
            val genreView = findViewById<TextView>(R.id.genreView);
            val descView = findViewById<TextView>(R.id.descView);
            textView.text = movie.title;
            genreView.text = movie.genres?.joinToString { it.name+", " };
            dateView.text = movie.releaseDate;
            descView.text = movie.overview;


            SERVICE.searchMovieCredits(movie.id, FR).enqueue(object : Callback<Credits?> {
                override fun onResponse(call: Call<Credits?>, response: Response<Credits?>) {
                    val it = response.body();
                    if (it != null) {
                        actorView.text = it.cast.joinToString { it.name+", " }
                    }
                }
                override fun onFailure(call: Call<Credits?>, t: Throwable) {
                    t.printStackTrace()
                }
            })


            if(movie.genresIds != null) SERVICE.movieGenre(FR).enqueue(object : Callback<Genre.GenreList?> {
                override fun onResponse(call: Call<Genre.GenreList?>, response: Response<Genre.GenreList?>) {
                    val it = response.body();
                    print("GENRES")
                    if (it != null) {
                        println(it.genres)
                        genreView.text = it.genres.filter { movie.genresIds.contains(it.id) }.joinToString { "${it.name}, " }
                    }
                }
                override fun onFailure(call: Call<Genre.GenreList?>, t: Throwable) {
                    t.printStackTrace()
                }
            })


            if (movie.posterPath != null){
                val size = 300
                Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w$size${movie.posterPath}")
                    .into(postView)
            }
        }

        // Set up the back button to navigate back to the previous activity
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle the back button click
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}