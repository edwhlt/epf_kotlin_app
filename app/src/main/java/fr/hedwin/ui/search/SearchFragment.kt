package fr.hedwin.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.material.textfield.TextInputLayout
import fr.hedwin.databinding.FragmentSearchBinding
import fr.hedwin.tmdb.`object`.DbMovie
import fr.hedwin.tmdb.retrofit.TMDB
import fr.hedwin.ui.EndlessScrollListener
import fr.hedwin.ui.MovieActivity
import fr.hedwin.ui.adapter.MovieAdapter

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var page = 1;
    private val adapter = MovieAdapter(mutableListOf<DbMovie>());

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val text: TextInputLayout = binding.editTextTextPersonName
        val load: ProgressBar = binding.progressBar;
        val searchButton: ImageButton = binding.imageButton;
        val listView: RecyclerView = binding.listResult

        val layoutManager = LinearLayoutManager(requireContext())
        listView.isClickable = true;
        listView.layoutManager = layoutManager
        adapter.setOnClick(onClick = {
            println(it);
            val intent = Intent(requireActivity(), MovieActivity::class.java)
            intent.putExtra("movie", ObjectMapper().writeValueAsString(it))
            startActivity(intent)
        })
        listView.adapter = adapter;

        val scrollListener = object : EndlessScrollListener(layoutManager) {
            override fun onLoadMore() {
                page += 1;
                searchFilm(text.editText?.text.toString(), page, adapter, load);
            }
        }
        listView.addOnScrollListener(scrollListener)

        searchButton.setOnClickListener {
            load.visibility = View.VISIBLE;
            page = 1;
            adapter.clear();
            searchFilm(text.editText?.text.toString(), page, adapter, load);
        }

        text.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                searchButton.requestFocus()
                return@setOnKeyListener true
            }
            false
        }

        return root;
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun searchFilm(text: String, page: Int, adapter: MovieAdapter, progressBar: ProgressBar){
        TMDB.searchMovie(text, page)
            .then {
                println(it.results)
                requireActivity().runOnUiThread {
                    adapter.addMovies(it.results)
                }
            }
            .error { exception ->
                exception.printStackTrace();
            }
            .thenFinally {
                progressBar.visibility = View.INVISIBLE;
            }
    }


}