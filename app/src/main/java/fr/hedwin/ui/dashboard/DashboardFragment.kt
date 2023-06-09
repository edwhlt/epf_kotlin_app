package fr.hedwin.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.databind.ObjectMapper
import fr.hedwin.R
import fr.hedwin.databinding.FragmentDashboardBinding
import fr.hedwin.tmdb.`object`.DbMovie
import fr.hedwin.ui.MovieActivity
import fr.hedwin.ui.adapter.MovieAdapter
import fr.hedwin.ui.adapter.MovieAdapterEmpty
import java.io.File
import java.io.IOException
import java.lang.Exception

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val listView: RecyclerView = binding.listFavorite
        listView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter.setOnClick(onClick = {
            println(it);
            val intent = Intent(requireActivity(), MovieActivity::class.java)
            intent.putExtra("movie", ObjectMapper().writeValueAsString(it))
            startActivity(intent)
        })
        listView.adapter = adapter;

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        lateinit var appContext: Context
        fun setContext(context: Context) {
            appContext = context.applicationContext
        }
        val adapter = MovieAdapterEmpty(mutableListOf<DbMovie>(), R.layout.list_movie_favorite);
        fun addMovie(movie: DbMovie){
            if (!containsMovie(movie)) {
                adapter.addMovies(listOf(movie))
                saveMovies();
            };
            else throw Exception("Film déjà ajouté au favoris !")
        }
        fun containsMovie(dbMovie: DbMovie): Boolean {
            return adapter.contains(dbMovie);
        }
        fun removeMovie(movie: DbMovie){
            adapter.removeMovie(movie);
            saveMovies();
        }
        private fun saveMovies(){
            val file = File(appContext.filesDir, "favorites.json")
            try {
                ObjectMapper().writeValue(file, adapter.listId());
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}