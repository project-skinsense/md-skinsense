package com.dicoding.picodiploma.loginwithanimation.view.detail

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.picodiploma.loginwithanimation.R
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
        val result = intent.getStringExtra(EXTRA_RESULT)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val prevention = intent.getStringExtra(EXTRA_PREVENTION)

        val detailImageView: ImageView = findViewById(R.id.detailImageView)
        val resultTextView: TextView = findViewById(R.id.resultTextView)
        val descTextView: TextView = findViewById(R.id.descTextView)
        val prevTextView: TextView = findViewById(R.id.prevTextView)

        resultTextView.text = result
        descTextView.text = description
        prevTextView.text = prevention


        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.skin_logo)
                .into(detailImageView)
        }

        val backButton: ImageButton = findViewById(R.id.action_back)

        backButton.setOnClickListener {
             finish()
        }
    }

    companion object {
        const val EXTRA_RESULT = "EXTRA_RESULT"
        const val EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL"
        const val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
        const val EXTRA_PREVENTION = "EXTRA_PREVENTION"
        const val EXTRA_SCORE = "EXTRA_SCORE"
    }
}