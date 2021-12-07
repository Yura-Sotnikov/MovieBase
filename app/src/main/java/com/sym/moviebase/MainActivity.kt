package com.sym.moviebase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO(4) Создайте экран, где будет отображаться список Избранного
//
// TODO(5) Сделайте так, чтобы из списка избранного можно было удалять элементы
//
// TODO(6) Добавьте поддержку альбомной ориентации. Интерфейс должен отличаться.
//  Например, в портретной 1 фильм в строке списка, а в альбомной 2-3
//
//
// TODO(7) Создайте кастомный диалог подтверждения при выходе из приложения при нажатии
//  кнопки back (использовать метод onBackPressed)
//
// TODO(8) *Написать собственный ItemDecoration
//
// TODO(9) *Самостоятельно изучите RecyclerView.ItemAnimator, создайте свои собственные анимации


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        private const val EXTRA_SELECTED_MOVIE = "EXTRA_SELECTED_MOVIE"
        private const val EXTRA_FAVORITE_MOVIES = "EXTRA_FAVORITE_MOVIES"
    }

    //Init test data
    private val movies: MutableList<MovieItem> = initTestData()
    private var favoriteMovies: ArrayList<MovieItem> = arrayListOf()

    var selectedMovie: Int = -1

    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerView) }

    private val detailActivityContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d(
                    "RESULT_DETAIL_ACTIVITY",
                    "Like this content: ${result.data?.getStringExtra(DetailActivity.RESULT_LIKE)}; Comment: ${
                        result.data?.getStringExtra(DetailActivity.RESULT_COMMENT)
                    }; $selectedMovie"
                )
            }

            this.recyclerView.adapter?.notifyItemRangeChanged(0, movies.size)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Restore save state
        savedInstanceState?.getInt(EXTRA_SELECTED_MOVIE)?.let {
            if (it != -1) selectedMovie = it
        }

        savedInstanceState?.getParcelableArrayList<MovieItem>(EXTRA_FAVORITE_MOVIES)?.let {
            favoriteMovies = it
        }

        initButtonListeners()

        setUpRecyclerView(recyclerView)


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(EXTRA_SELECTED_MOVIE, selectedMovie)
        outState.putParcelableArrayList(EXTRA_FAVORITE_MOVIES, favoriteMovies)
    }

    //Custom functions

    //Init RecyclerView in activity_main.xml
    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        val adapter = MovieItemsAdapter(
            movies,
            favoriteMovies,
            selectedMovie,
            object : MovieItemsAdapter.MovieClickListener {
                override fun onDetailsClick(movie: MovieItem, position: Int, selectMovie: Int) {
                    DetailActivity.launchActivity(this@MainActivity, detailActivityContract, movie)
                    this@MainActivity.selectedMovie = position
                    (this@MainActivity.recyclerView.adapter as MovieItemsAdapter).currentMovie =
                        position
                }

                override fun onFavoriteClick(movie: MovieItem, position: Int) {
                    if (movie in favoriteMovies) {
                        favoriteMovies.remove(movie)

                        Toast.makeText(
                            this@MainActivity,
                            "Remove from favorite movies",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        favoriteMovies.add(movie)

                        Toast.makeText(this@MainActivity, "Add favorite movies", Toast.LENGTH_SHORT)
                            .show()

                    }

                    recyclerView.adapter?.notifyItemChanged(position)
                }
            })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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

