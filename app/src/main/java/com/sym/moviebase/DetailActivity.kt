package com.sym.moviebase

import android.app.Activity
import android.content.Intent
import android.icu.text.CaseMap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.button.MaterialButton

class DetailActivity : AppCompatActivity(R.layout.activity_detail) {

    companion object {
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
        const val EXTRA_POSTER = "EXTRA_POSTER"

        const val RESULT_LIKE = "RESULT_LIKE"
        const val RESULT_COMMENT = "RESULT_COMMENT"


        fun launchActivity(
            activity: Activity,
            contract: ActivityResultLauncher<Intent>,
            title: String,
            description: String,
            poster: Int
        ) {
            val intentForResult = Intent(activity, DetailActivity::class.java).apply {
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_DESCRIPTION, description)
                putExtra(EXTRA_POSTER, poster)
            }

            contract.launch(intentForResult)
        }
    }


    private var isChecked: Boolean = false
    private val btn_Like by lazy { findViewById<MaterialButton>(R.id.btn_Like) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.getStringExtra(EXTRA_TITLE)
            ?.let { findViewById<TextView>(R.id.tv_title_detail).text = it }
        intent.getStringExtra(EXTRA_DESCRIPTION)
            ?.let { findViewById<TextView>(R.id.tv_description_detail).text = it }
        intent.getIntExtra(EXTRA_POSTER, 0).let {
            findViewById<ImageView>(R.id.iv_poster_detail).setImageResource(it)
        }

        initButtonListener()

    }

    //Init button Listener
    private fun initButtonListener() {
        //Button Like
        btn_Like.setOnClickListener {
            isChecked = !isChecked

            btn_Like.icon

            if (isChecked) {
                btn_Like.icon = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_baseline_favorite_24,
                    theme
                )
            } else {
                btn_Like.icon = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_baseline_favorite_border_24,
                    theme
                )
            }

        }

        //Button Done
        findViewById<Button>(R.id.btn_Done).setOnClickListener {
            //Send result & close activity
            val result = Intent().apply {
                putExtra(RESULT_LIKE, isChecked.toString())
                putExtra(
                    RESULT_COMMENT, findViewById<EditText>(R.id.et_comment).text.toString()
                )
            }
            setResult(Activity.RESULT_OK, result)
            finish()
        }
    }
}

