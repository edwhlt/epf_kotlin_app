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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fasterxml.jackson.databind.ObjectMapper
import fr.hedwin.R
import fr.hedwin.tmdb.`object`.DbMovie
import fr.hedwin.tmdb.`object`.ResultsPage
import fr.hedwin.tmdb.retrofit.TMDB.FR
import fr.hedwin.tmdb.retrofit.TMDB.SERVICE
import fr.hedwin.ui.MovieRecommandedActivity
import fr.hedwin.ui.dashboard.DashboardFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.function.Consumer


class MovieAdapter(private val films: MutableList<DbMovie>, private val withBtn: Boolean) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val buttonFav: ImageButton = itemView.findViewById(R.id.buttonFav)
        val buttonRecomm: ImageButton = itemView.findViewById(R.id.buttonRecomm)
        val image: ImageView = itemView.findViewById(R.id.imageView)
    }

    private var onClick: Consumer<DbMovie> = Consumer{};

    fun setOnClick(onClick: Consumer<DbMovie>){
        this.onClick = onClick;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return films.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val film = films[position]

        if(!withBtn){
            holder.buttonFav.isVisible = false;
            holder.buttonRecomm.isVisible = false;
        }

        holder.titleTextView.text = film.title
        holder.descriptionTextView.text = film.overview

        val originalTintList = holder.buttonFav.imageTintList

        if (DashboardFragment.containsMovie(film)) {
            holder.buttonFav.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.favorite_color))
        }

        holder.buttonFav.setOnClickListener {
            if(DashboardFragment.containsMovie(film)){
                DashboardFragment.removeMovie(film)
                holder.buttonFav.imageTintList = originalTintList;
                Toast.makeText(holder.itemView.context, "Film supprimé des favoris !", Toast.LENGTH_SHORT).show();
            }
            else try{
                DashboardFragment.addMovie(film)
                holder.buttonFav.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.favorite_color))
                Toast.makeText(holder.itemView.context, "Film ajouté aux favoris !", Toast.LENGTH_SHORT).show();
            }catch (e: Exception){
                Toast.makeText(holder.itemView.context, e.message, Toast.LENGTH_SHORT).show();
            }
        }

        holder.buttonRecomm.setOnClickListener {
            SERVICE.recommandedMovieFrom(film.id, FR).enqueue(object : Callback<ResultsPage<DbMovie>?> {
                override fun onResponse(call: Call<ResultsPage<DbMovie>?>, response: Response<ResultsPage<DbMovie>?>) {
                    val it = response.body();

                    if (it != null && it.results.isNotEmpty()) {
                        val intent = Intent(holder.itemView.context, MovieRecommandedActivity::class.java)
                        intent.putExtra("movieName", film.title)
                        intent.putExtra("movies", ObjectMapper().writeValueAsString(it))
                        holder.itemView.context.startActivity(intent)
                    }else{
                        Toast.makeText(holder.itemView.context, "Aucun film recommandé pour ce film !", Toast.LENGTH_SHORT).show();
                    }
                }
                override fun onFailure(call: Call<ResultsPage<DbMovie>?>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }

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