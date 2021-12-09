package com.sym.moviebase

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.coroutines.Continuation
import com.sym.moviebase.MovieItemsAdapter.MovieClickListener as MovieClickListener

// TODO(8) *Написать собственный ItemDecoration
//
// TODO(9) *Самостоятельно изучите RecyclerView.ItemAnimator, создайте свои собственные анимации


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        private const val EXTRA_SELECTED_MOVIE = "EXTRA_SELECTED_MOVIE"
        private const val EXTRA_FAVORITE_MOVIES = "EXTRA_FAVORITE_MOVIES"
        private const val EXTRA_CURRENT_TAB = "EXTRA_CURRENT_TAB"

        private const val TAB_MAIN = 0
        private const val TAB_FAVORITE = 1
    }

    //Init test data
    private val movies: MutableList<MovieItem> = initTestData()
    private var favoriteMovies: ArrayList<MovieItem> = arrayListOf()

    var selectedMovie: Int = -1

    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerView) }
    private val bottomNavView by lazy { findViewById<BottomNavigationView>(R.id.bottom_nav) }

    private lateinit var mainAdapter: MovieItemsAdapter
    private lateinit var favoriteAdapter: MovieItemsAdapter

    private var currentTab = TAB_MAIN

    private val detailActivityContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val favorite: Boolean? =
                    result.data?.getBooleanExtra(DetailActivity.RESULT_LIKE, false)
                Log.d(
                    "RESULT_DETAIL_ACTIVITY",
                    "Like this content: $favorite; Comment: ${
                        result.data?.getStringExtra(DetailActivity.RESULT_COMMENT)
                    };"
                )
                if (favorite == true) favoriteMovies.add(movies[selectedMovie])
            }

            this.recyclerView.adapter?.notifyItemRangeChanged(0, movies.size)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Restore save state
        savedInstanceState?.apply {
            getInt(EXTRA_SELECTED_MOVIE).let { if (it != -1) selectedMovie = it }
            getParcelableArrayList<MovieItem>(EXTRA_FAVORITE_MOVIES)?.let {
                favoriteMovies = it
            }
            getInt(EXTRA_CURRENT_TAB).let { currentTab = it }
        }

        initButtonListeners()
        setUpRecyclerView()
        initBottomNav()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.apply {
            putInt(EXTRA_SELECTED_MOVIE, selectedMovie)
            putInt(EXTRA_CURRENT_TAB, currentTab)
            putParcelableArrayList(EXTRA_FAVORITE_MOVIES, favoriteMovies)
        }
    }

    override fun onBackPressed() {

        QuitDialog(this, object : QuitDialog.QuitDialogClickListeners {
            override fun onOKClick() {
                closeActivity()
            }
        }).show()
    }

    fun closeActivity() {

        super.onBackPressed()
    }


    //Custom functions
    private fun initBottomNav() {
        bottomNavView.setOnItemSelectedListener {
            when (it.title) {
                "Main" -> {
                    recyclerView.adapter = mainAdapter
                    currentTab = TAB_MAIN
                }
                "Favorite" -> {
                    recyclerView.adapter = favoriteAdapter
                    currentTab = TAB_FAVORITE
                }
            }
            true
        }
    }

    //Init RecyclerView in activity_main.xml
    private fun setUpRecyclerView() {

        val adapterListeners = object : MovieClickListener {
            override fun onDetailsClick(movie: MovieItem, position: Int, selectMovie: Int) {
                DetailActivity.launchActivity(this@MainActivity, detailActivityContract, movie)
                this@MainActivity.selectedMovie = position
                (this@MainActivity.recyclerView.adapter as MovieItemsAdapter).currentMovie =
                    position
            }

            override fun onFavoriteClick(movie: MovieItem, position: Int) {
                if (movie in favoriteMovies)
                    favoriteMovies.remove(movie)
                else
                    favoriteMovies.add(movie)

                recyclerView.adapter?.notifyItemChanged(position)
            }

            override fun onRemoveClick(movie: MovieItem, position: Int) {
                if (movie in favoriteMovies) favoriteMovies.remove(movie)

                recyclerView.adapter?.notifyItemRemoved(position)
            }
        }

        mainAdapter = MovieItemsAdapter(
            movies,
            favoriteMovies,
            selectedMovie,
            MovieItemsAdapter.TYPE_LIST_ALL_MOVIES,
            adapterListeners
        )

        favoriteAdapter = MovieItemsAdapter(
            favoriteMovies,
            favoriteMovies,
            selectedMovie,
            MovieItemsAdapter.TYPE_LIST_FAVORITE_MOVIES,
            adapterListeners
        )
        if (currentTab == TAB_MAIN) recyclerView.adapter = mainAdapter
        else recyclerView.adapter = favoriteAdapter

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            recyclerView.layoutManager =
                GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        else
            recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


    }

    private fun initButtonListeners() {
        //FAB
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            //Invite friend
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Go to my app!")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }
}


