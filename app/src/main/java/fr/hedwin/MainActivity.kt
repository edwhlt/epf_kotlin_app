package fr.hedwin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.hedwin.databinding.ActivityMainBinding
import fr.hedwin.tmdb.`object`.DbMovie
import fr.hedwin.tmdb.retrofit.TMDB.FR
import fr.hedwin.tmdb.retrofit.TMDB.SERVICE
import fr.hedwin.ui.dashboard.DashboardFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadFavorite();
        DashboardFragment.setContext(applicationContext)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
    }

    fun loadFavorite(){
        val file = File(applicationContext.filesDir, "favorites.json")
        try {
            val listId = ObjectMapper().readValue(file, object : TypeReference<List<Int?>?>() {})
            listId?.forEach {
                SERVICE.searchMovie(it, FR).enqueue(object : Callback<DbMovie?> {
                    override fun onResponse(call: Call<DbMovie?>, response: Response<DbMovie?>) {
                        print(it)
                        print("=")
                        println(response.body())
                        response.body()?.let { it1 -> DashboardFragment.addMovie(it1) }
                    }
                    override fun onFailure(call: Call<DbMovie?>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}
