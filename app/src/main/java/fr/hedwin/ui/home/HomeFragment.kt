package fr.hedwin.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.databind.ObjectMapper
import fr.hedwin.databinding.FragmentHomeBinding
import fr.hedwin.tmdb.`object`.DbMovie
import fr.hedwin.tmdb.retrofit.TMDB
import fr.hedwin.ui.MovieActivity
import fr.hedwin.ui.adapter.MovieAdapter
import fr.hedwin.ui.adapter.MovieAdapterEmpty

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

        val listView: RecyclerView = binding.listResult

        listView.isClickable = true;
        listView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        TMDB.trendingMovies()
            .then {
                requireActivity().runOnUiThread {
                    val adapter = MovieAdapterEmpty(it.results.toMutableList());
                    adapter.setOnClick(onClick =  { movie ->
                        val intent = Intent(requireActivity(), MovieActivity::class.java)
                        intent.putExtra("movie", ObjectMapper().writeValueAsString(movie))
                        startActivity(intent)
                    });
                    listView.adapter = adapter;
                }
            }
            .error { exception ->
                exception.printStackTrace();
            }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}