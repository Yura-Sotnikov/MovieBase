package com.sym.moviebase

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    companion object {
        private const val EXTRA_SELECTED_MOVIE = "EXTRA_SELECTED_MOVIE"
    }

    private var selectedMovie = -1
    private var DefaultBackgroundColor = 0

    private val cards by lazy {
        arrayOf(
            arrayOf(
                0,
                findViewById<CardView>(R.id.card_01),
                findViewById<ImageView>(R.id.iv_poster_01),
                findViewById<TextView>(R.id.tv_title_01),
                findViewById<TextView>(R.id.tv_description_01),
                findViewById<Button>(R.id.btnDetails_01),
                R.drawable.mb_poster_01
            ),
            arrayOf(
                1,
                findViewById<CardView>(R.id.card_02),
                findViewById<ImageView>(R.id.iv_poster_02),
                findViewById<TextView>(R.id.tv_title_02),
                findViewById<TextView>(R.id.tv_description_02),
                findViewById<Button>(R.id.btnDetails_02),
                R.drawable.mb_poster_02
            ),
            arrayOf(
                2,
                findViewById<CardView>(R.id.card_03),
                findViewById<ImageView>(R.id.iv_poster_03),
                findViewById<TextView>(R.id.tv_title_03),
                findViewById<TextView>(R.id.tv_description_03),
                findViewById<Button>(R.id.btnDetails_03),
                R.drawable.mb_poster_03
            ),
            arrayOf(
                3,
                findViewById<CardView>(R.id.card_04),
                findViewById<ImageView>(R.id.iv_poster_04),
                findViewById<TextView>(R.id.tv_title_04),
                findViewById<TextView>(R.id.tv_description_04),
                findViewById<Button>(R.id.btnDetails_04),
                R.drawable.mb_poster_04
            ),
            arrayOf(
                4,
                findViewById<CardView>(R.id.card_05),
                findViewById<ImageView>(R.id.iv_poster_05),
                findViewById<TextView>(R.id.tv_title_05),
                findViewById<TextView>(R.id.tv_description_05),
                findViewById<Button>(R.id.btnDetails_05),
                R.drawable.mb_poster_05
            ),
            arrayOf(
                5,
                findViewById<CardView>(R.id.card_06),
                findViewById<ImageView>(R.id.iv_poster_06),
                findViewById<TextView>(R.id.tv_title_06),
                findViewById<TextView>(R.id.tv_description_06),
                findViewById<Button>(R.id.btnDetails_06),
                R.drawable.mb_poster_06
            ),
            arrayOf(
                6,
                findViewById<CardView>(R.id.card_07),
                findViewById<ImageView>(R.id.iv_poster_07),
                findViewById<TextView>(R.id.tv_title_07),
                findViewById<TextView>(R.id.tv_description_07),
                findViewById<Button>(R.id.btnDetails_07),
                R.drawable.mb_poster_07
            ),
            arrayOf(
                7,
                findViewById<CardView>(R.id.card_08),
                findViewById<ImageView>(R.id.iv_poster_08),
                findViewById<TextView>(R.id.tv_title_08),
                findViewById<TextView>(R.id.tv_description_08),
                findViewById<Button>(R.id.btnDetails_08),
                R.drawable.mb_poster_08
            ),
            arrayOf(
                8,
                findViewById<CardView>(R.id.card_09),
                findViewById<ImageView>(R.id.iv_poster_09),
                findViewById<TextView>(R.id.tv_title_09),
                findViewById<TextView>(R.id.tv_description_09),
                findViewById<Button>(R.id.btnDetails_09),
                R.drawable.mb_poster_09
            ),
            arrayOf(
                9,
                findViewById<CardView>(R.id.card_10),
                findViewById<ImageView>(R.id.iv_poster_10),
                findViewById<TextView>(R.id.tv_title_10),
                findViewById<TextView>(R.id.tv_description_10),
                findViewById<Button>(R.id.btnDetails_10),
                R.drawable.mb_poster_10
            ),
            arrayOf(
                10,
                findViewById<CardView>(R.id.card_11),
                findViewById<ImageView>(R.id.iv_poster_11),
                findViewById<TextView>(R.id.tv_title_11),
                findViewById<TextView>(R.id.tv_description_11),
                findViewById<Button>(R.id.btnDetails_11),
                R.drawable.mb_poster_11
            ),
            arrayOf(
                11,
                findViewById<CardView>(R.id.card_12),
                findViewById<ImageView>(R.id.iv_poster_12),
                findViewById<TextView>(R.id.tv_title_12),
                findViewById<TextView>(R.id.tv_description_12),
                findViewById<Button>(R.id.btnDetails_12),
                R.drawable.mb_poster_12
            ),
            arrayOf(
                12,
                findViewById<CardView>(R.id.card_13),
                findViewById<ImageView>(R.id.iv_poster_13),
                findViewById<TextView>(R.id.tv_title_13),
                findViewById<TextView>(R.id.tv_description_13),
                findViewById<Button>(R.id.btnDetails_13),
                R.drawable.mb_poster_13
            ),
        )
    }

    private val detailActivityContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("RESULT_DETAIL_ACTIVITY", "Like this content: ${result.data?.getStringExtra(DetailActivity.RESULT_LIKE)}; Comment: ${result.data?.getStringExtra(DetailActivity.RESULT_COMMENT)}")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DefaultBackgroundColor = (cards[1][1] as CardView).cardBackgroundColor.defaultColor
        Log.d("Default_color", DefaultBackgroundColor.toString())

        initButtonListeners()

        //Restore save state
        savedInstanceState?.getInt(EXTRA_SELECTED_MOVIE)?.let {
            if (it != -1) {
                (cards[it][1] as CardView).setCardBackgroundColor(Color.LTGRAY)
                selectedMovie = it
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(EXTRA_SELECTED_MOVIE, selectedMovie)
    }

    //Custom functions
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

        //Buttons detail
        for (movie in cards) {
            val index: Int = movie[0] as Int
            val card: CardView = movie[1] as CardView
            val title: TextView = movie[3] as TextView
            val description: TextView = movie[4] as TextView
            val btn: Button = movie[5] as Button
            val intPoster: Int = movie[6] as Int


            btn.setOnClickListener {
                if (selectedMovie != -1) {
                    (cards[selectedMovie][1] as CardView).setCardBackgroundColor(
                        DefaultBackgroundColor
                    )
                }

                selectedMovie = index
                card.setCardBackgroundColor(Color.LTGRAY)

                //Launch detail activity
                DetailActivity.launchActivity(
                    this,
                    detailActivityContract,
                    title.text.toString(),
                    description.text.toString(),
                    intPoster
                )
            }
        }
    }
}

