package fr.hedwin.ui.adapter

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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fasterxml.jackson.databind.ObjectMapper
import fr.hedwin.R
import fr.hedwin.tmdb.`object`.DbMovie
import fr.hedwin.tmdb.`object`.ResultsPage
import fr.hedwin.tmdb.retrofit.TMDB
import fr.hedwin.ui.MovieRecommandedActivity
import fr.hedwin.ui.dashboard.DashboardFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.function.Consumer

class MovieAdapterEmpty(private val films: MutableList<DbMovie>, private val ressouce: Int) : RecyclerView.Adapter<MovieAdapterEmpty.MovieViewHolder>() {
    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val image: ImageView = itemView.findViewById(R.id.imageView)
    }

    private var onClick: Consumer<DbMovie> = Consumer{};

    fun setOnClick(onClick: Consumer<DbMovie>){
        this.onClick = onClick;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(ressouce, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return films.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val film = films[position]

        holder.titleTextView.text = film.title

        holder.itemView.setOnClickListener {
            onClick.accept(film);
        }

        if (film.posterPath != null){
            val size = 300
            Glide.with(holder.itemView.context)
                .load("https://image.tmdb.org/t/p/w$size${film.posterPath}")
                .into(holder.image)
        }
    }

    fun contains(dbMovie: DbMovie): Boolean {
        return films.contains(dbMovie);
    }


    fun addMovies(newMovies: List<DbMovie>) {
        val insertIndex = films.size
        films.addAll(newMovies)
        notifyItemRangeInserted(insertIndex, newMovies.size)
    }

    fun removeMovie(movie: DbMovie) {
        val position = films.indexOf(movie)
        if (position in 0 until films.size) {
            films.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear(){
        val itemCount = films.size
        films.clear();
        notifyItemRangeRemoved(0, itemCount)
    }

    fun listId(): List<Int> {
        return films.map { it.id }.toList();
    }

}