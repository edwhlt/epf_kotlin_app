package fr.hedwin.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.hedwin.R
import fr.hedwin.tmdb.`object`.DbMovie
import fr.hedwin.tmdb.`object`.ResultsPage
import fr.hedwin.ui.adapter.MovieAdapter
import fr.hedwin.ui.adapter.MovieAdapterEmpty

class MovieRecommandedActivity  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommanded)

        // Retrieve the data passed from the MainActivity
        val movies = ObjectMapper().readValue(intent.getStringExtra("movies"), object : TypeReference<ResultsPage<DbMovie>?>() {})

        if(movies != null){
            title = "Films recommandés"
            val listView: RecyclerView = findViewById(R.id.listResult)

            listView.isClickable = true;
            listView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            listView.adapter = MovieAdapter(movies.results.toMutableList(), false);
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