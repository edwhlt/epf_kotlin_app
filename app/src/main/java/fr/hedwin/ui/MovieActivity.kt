package fr.hedwin.ui

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.hedwin.R
import fr.hedwin.tmdb.`object`.Credits
import fr.hedwin.tmdb.`object`.DbMovie
import fr.hedwin.tmdb.`object`.Genre
import fr.hedwin.tmdb.`object`.ResultsPage
import fr.hedwin.tmdb.retrofit.TMDB.FR
import fr.hedwin.tmdb.retrofit.TMDB.SERVICE
import fr.hedwin.ui.dashboard.DashboardFragment
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

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


            val favorisBtn = findViewById<ImageButton>(R.id.buttonFav2);
            val originalTintList = favorisBtn.imageTintList;
            val recommBtn = findViewById<ImageButton>(R.id.buttonRecomm2);
            val videoBtn = findViewById<ImageButton>(R.id.buttonVideo);

            if (DashboardFragment.containsMovie(movie)) {
                favorisBtn.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.favorite_color))
            }

            favorisBtn.setOnClickListener {
                if(DashboardFragment.containsMovie(movie)){
                    DashboardFragment.removeMovie(movie)
                    favorisBtn.imageTintList = originalTintList;
                    Toast.makeText(applicationContext, "Film supprimé des favoris !", Toast.LENGTH_SHORT).show();
                }
                else try{
                    DashboardFragment.addMovie(movie)
                    favorisBtn.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.favorite_color))
                    Toast.makeText(applicationContext, "Film ajouté aux favoris !", Toast.LENGTH_SHORT).show();
                }catch (e: Exception){
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show();
                }
            }

            recommBtn.setOnClickListener {
                SERVICE.recommandedMovieFrom(movie.id, FR).enqueue(object : Callback<ResultsPage<DbMovie>?> {
                    override fun onResponse(call: Call<ResultsPage<DbMovie>?>, response: Response<ResultsPage<DbMovie>?>) {
                        val it = response.body();

                        if (it != null && it.results.isNotEmpty()) {
                            val intent = Intent(applicationContext, MovieRecommandedActivity::class.java)
                            intent.putExtra("movies", ObjectMapper().writeValueAsString(it))
                            startActivity(intent)
                        }else{
                            Toast.makeText(applicationContext, "Aucun film recommandé pour ce film !", Toast.LENGTH_SHORT).show();
                        }
                    }
                    override fun onFailure(call: Call<ResultsPage<DbMovie>?>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }

            videoBtn.setOnClickListener {
                movie.videos.then {
                    openYouTubeVideo(it.videoList.get(0).key)
                }
            }

            textView.text = movie.title;
            genreView.text = movie.genres?.joinToString { it.name };
            dateView.text = movie.releaseDate;
            descView.text = movie.overview;


            SERVICE.searchMovieCredits(movie.id, FR).enqueue(object : Callback<Credits?> {
                override fun onResponse(call: Call<Credits?>, response: Response<Credits?>) {
                    val it = response.body();
                    if (it != null) {
                        actorView.text = it.cast.joinToString { it.name }
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
                        genreView.text = it.genres.filter { movie.genresIds.contains(it.id) }.joinToString { it.name }
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

    fun openYouTubeVideo(videoId: String) {
        val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
        val intentWeb = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$videoId"))

        try {
            startActivity(intentApp)
        } catch (ex: ActivityNotFoundException) {
            startActivity(intentWeb)
        }
    }
}