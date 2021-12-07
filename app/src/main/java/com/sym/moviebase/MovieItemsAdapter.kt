package com.sym.moviebase

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class MovieItemsAdapter(
    private val movies: List<MovieItem>,
    var favoriteMovies: List<MovieItem>,
    var currentMovie: Int,
    private val clickListener: MovieClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieViewHolder) {
            val item = movies[position]
            val favorite = item in favoriteMovies

            holder.bind(item, position, currentMovie, favorite, clickListener)
        }
    }

    override fun getItemCount(): Int = movies.size

    interface MovieClickListener {
        fun onDetailsClick(movie: MovieItem, position: Int, selectMovie: Int)
        fun onFavoriteClick(movie: MovieItem, position: Int)
    }

    class MovieViewHolder internal constructor(movieItemView: View) :
        RecyclerView.ViewHolder(movieItemView) {

        private val _mcCard: MaterialCardView = movieItemView.findViewById(R.id.card)
        private val _ivPoster: ImageView = movieItemView.findViewById(R.id.iv_poster)
        private val _tvTitle: TextView = movieItemView.findViewById(R.id.tv_title)
        private val _tvDescription: TextView = movieItemView.findViewById(R.id.tv_description)
        private val _btnDetails: MaterialButton = movieItemView.findViewById(R.id.btn_Details)
        private val _btnFavorite: MaterialButton = movieItemView.findViewById(R.id.btn_Favorite)

        fun bind(
            movie: MovieItem,
            position: Int,
            selectMovie: Int,
            favorite: Boolean,
            clickListener: MovieClickListener
        ) {
            _tvTitle.text = movie.title
            _tvDescription.text = movie.description
            _ivPoster.setImageResource(movie.poster)

            _mcCard.isPressed = position == selectMovie
            _btnFavorite.setIconResource(if (favorite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24)

            _btnDetails.setOnClickListener {
                clickListener.onDetailsClick(
                    movie,
                    position,
                    selectMovie
                )
            }
            _btnFavorite.setOnClickListener { clickListener.onFavoriteClick(movie, position) }
        }
    }
}