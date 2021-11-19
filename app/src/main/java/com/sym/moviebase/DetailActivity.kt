package com.sym.moviebase

import android.app.Activity
import android.content.Intent
import android.icu.text.CaseMap
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity(R.layout.activity_detail) {
    companion object {
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
        const val EXTRA_POSTER = "EXTRA_POSTER"

        fun launchActivity(activity: Activity, title: String, description: String, poster: Int) {
            Intent(activity, DetailActivity::class.java).apply {
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_DESCRIPTION, description)
                putExtra(EXTRA_POSTER, poster)

                activity.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.getStringExtra(EXTRA_TITLE)
            ?.let { findViewById<TextView>(R.id.tv_title_detail).text = it }
        intent.getStringExtra(EXTRA_DESCRIPTION)
            ?.let { findViewById<TextView>(R.id.tv_description_detail).text = it }
        intent.getIntExtra(EXTRA_POSTER, 0).let {
            findViewById<ImageView>(R.id.iv_poster_detail).setImageResource(it)
        }

    }
}

