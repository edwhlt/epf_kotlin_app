package fr.hedwin.ui.adapter

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fasterxml.jackson.databind.ObjectMapper
import fr.hedwin.R
import fr.hedwin.tmdb.`object`.DbMovie
import fr.hedwin.tmdb.`object`.Genre
import fr.hedwin.tmdb.`object`.ResultsPage
import fr.hedwin.tmdb.retrofit.TMDB
import fr.hedwin.ui.MovieActivity
import fr.hedwin.ui.MovieRecommandedActivity
import fr.hedwin.ui.dashboard.DashboardFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.function.Consumer

class GenreAdapter(private val genres: MutableList<Genre>, private val films: MutableList<DbMovie>) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {
    inner class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val listMovies: RecyclerView = itemView.findViewById(R.id.listResult)
    }

    private var onClick: Consumer<DbMovie> = Consumer{};

    fun setOnClick(onClick: Consumer<DbMovie>){
        this.onClick = onClick;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_genre_item, parent, false)
        return GenreViewHolder(view)
    }

    override fun getItemCount(): Int {
        return genres.size
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = genres[position]

        holder.titleTextView.text = genre.name
        holder.listMovies.isClickable = true;
        holder.listMovies.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)


        val adapter = MovieAdapterEmpty(films.filter { it.genresIds.contains(genre.id) }.toMutableList(), R.layout.list_movie);
        adapter.setOnClick(onClick =  { movie ->
            val intent = Intent(holder.itemView.context, MovieActivity::class.java)
            intent.putExtra("movie", ObjectMapper().writeValueAsString(movie))
            holder.itemView.context.startActivity(intent)
        });
        holder.listMovies.adapter = adapter;
    }

}