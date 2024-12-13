package com.dicoding.picodiploma.loginwithanimation.view.ui.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.R
import java.io.File

class ResultTestActivity : AppCompatActivity() {

    private lateinit var imageUri: Uri

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            imageUri = setupImageUri()
            cameraLauncher.launch(imageUri)
        } else {
            Toast.makeText(this, "Izin kamera diperlukan untuk mengambil gambar", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_test)

        val gender = intent.getStringExtra("gender")
        val age = intent.getIntExtra("age", -1)
        val skinCondition = intent.getStringExtra("skinCondition")
        val sensitiveSkin = intent.getStringExtra("sensitiveSkin")
        val symptomStart = intent.getStringExtra("symptomStart")
        val worstSymptom = intent.getStringExtra("worstSymptom")
        val skinDiseaseHistory = intent.getStringExtra("skinDiseaseHistory")
        val geneticSkinDisease = intent.getStringExtra("geneticSkinDisease")
        val skinMedicationHistory = intent.getStringExtra("skinMedicationHistory")
        val detailedDescription = intent.getStringExtra("detailedDescription")

        val resultText = """
            Jenis Kelamin: $gender
            Usia: $age
            Kondisi Kulit: $skinCondition
            Kulit Sensitif: $sensitiveSkin
            Gejala Penyakit Mulai Muncul Tanggal: $symptomStart
            Gejala Terburuk: $worstSymptom
            Memiliki Riwayat Penyakit Kulit: $skinDiseaseHistory
            Memiliki Penyakit Kulit Genetik: $geneticSkinDisease
            Obat Kulit Tertentu: $skinMedicationHistory
            Detail Gejala: $detailedDescription
        """.trimIndent()

        findViewById<TextView>(R.id.result_textview).text = resultText

        findViewById<ImageButton>(R.id.action_back).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.next_button).setOnClickListener {
            showImageSourceDialog()
        }
        setupImageUri()
    }

    private fun setupImageUri(): Uri {
        val timeStamp = System.currentTimeMillis().toString()
        val tempFile = File(getExternalFilesDir(null), "temp_image_$timeStamp.jpg")
        return FileProvider.getUriForFile(
            this,
            "com.dicoding.picodiploma.loginwithanimation.fileprovider",
            tempFile
        )
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            navigateToDisplayImageActivity(imageUri)
        } else {
            Toast.makeText(this, "Camera action canceled", Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            navigateToDisplayImageActivity(uri)
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Kamera", "Galeri", "Batalkan")
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Pilih Opsi")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        imageUri = setupImageUri()
                        cameraLauncher.launch(imageUri)
                    } else {
                        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
                1 -> galleryLauncher.launch("image/*")
                2 -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun navigateToDisplayImageActivity(uri: Uri) {
        val intent = Intent(this, DisplayImageActivity::class.java)
        intent.putExtra("imageUri", uri.toString())
        startActivity(intent)
    }
}
