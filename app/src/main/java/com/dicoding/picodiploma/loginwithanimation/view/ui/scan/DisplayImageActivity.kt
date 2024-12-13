package com.dicoding.picodiploma.loginwithanimation.view.ui.scan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.PredictResponse
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class DisplayImageActivity : AppCompatActivity() {

    private val resultTestViewModel by viewModels<ResultTestViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_image)

        val imageView: ImageView = findViewById(R.id.image_view)
        val resultButton: Button = findViewById(R.id.result_button)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        val imageUriString = intent.getStringExtra("imageUri")
        val imageUri = Uri.parse(imageUriString)

        resultTestViewModel.getUserEmail()

        val tempFile = File(getExternalFilesDir(null), "temp_image.jpg")
        contentResolver.openInputStream(imageUri)?.use { inputStream ->
            inputStream.copyTo(tempFile.outputStream())
        }
        Picasso.get()
            .load(imageUri)
            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
            .into(imageView)


        resultTestViewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            resultButton.isEnabled = !isLoading
        }

        resultTestViewModel.email.observe(this) { email ->
            if (!email.isNullOrEmpty()) {
                resultButton.setOnClickListener {
                    val filePart = MultipartBody.Part.createFormData(
                        "file", tempFile.name, tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    )
                    val userIdRequestBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
                    resultTestViewModel.uploadPrediction(filePart, userIdRequestBody)

                    resultTestViewModel.predictResult.observe(this) { response ->
                        navigateToMainActivity(response)
                    }

                    resultTestViewModel.error.observe(this) { error ->
                        Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Email tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMainActivity(response: PredictResponse) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("navigate_to_result", true)
            putExtra("predict_result", response.prediction)
        }
        startActivity(intent)
    }
}
