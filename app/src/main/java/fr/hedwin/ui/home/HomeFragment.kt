package fr.hedwin.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.databind.ObjectMapper
import fr.hedwin.databinding.FragmentHomeBinding
import fr.hedwin.tmdb.`object`.DbMovie
import fr.hedwin.tmdb.`object`.Genre.GenreList
import fr.hedwin.tmdb.`object`.ResultsPage
import fr.hedwin.tmdb.retrofit.TMDB
import fr.hedwin.tmdb.retrofit.TMDB.FR
import fr.hedwin.tmdb.retrofit.TMDB.SERVICE
import fr.hedwin.ui.MovieActivity
import fr.hedwin.ui.MovieRecommandedActivity
import fr.hedwin.ui.adapter.GenreAdapter
import fr.hedwin.ui.adapter.MovieAdapter
import fr.hedwin.ui.adapter.MovieAdapterEmpty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requireActivity().title = "Film populaire cette semaine"
        val listView: RecyclerView = binding.listResult

        listView.isClickable = true;
        listView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        SERVICE.movieGenre(FR).enqueue(object : Callback<GenreList?> {
            override fun onResponse(call: Call<GenreList?>, response: Response<GenreList?>) {
                val ger = response.body();
                if (ger != null) {
                    TMDB.trendingMovies().then { movies ->
                        val genres = movies.flatMap { it.genresIds }.distinct();
                        requireActivity().runOnUiThread {
                            val adapter = GenreAdapter(ger.genres.filter { genres.contains(it.id) }.toMutableList(), movies.toMutableList());
                            listView.adapter = adapter;
                        }
                    }
                }
            }
            override fun onFailure(call: Call<GenreList?>, t: Throwable) {
                t.printStackTrace()
            }
        })



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}