package com.dicoding.picodiploma.loginwithanimation.view.ui.scan

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.SkinHealthRequest
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import java.util.Calendar

class QuickTestActivity : AppCompatActivity() {

    private val viewModel by viewModels<QuickTestViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quick_test)

        val backButton: ImageButton = findViewById(R.id.action_back)
        val testButton: Button = findViewById(R.id.test_button)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        viewModel.getUserEmail()

        backButton.setOnClickListener {
            finish()
        }

        testButton.setOnClickListener {
            val gender = findViewById<Spinner>(R.id.gender_spinner).selectedItem.toString()
            val ageText = findViewById<EditText>(R.id.age_edit_text).text.toString()
            val skinCondition = findViewById<EditText>(R.id.skin_condition_edit_text).text.toString()
            val sensitiveSkin = findViewById<Spinner>(R.id.sensitive_skin_spinner).selectedItem.toString()
            val symptomStart = findViewById<EditText>(R.id.symptom_start_edit_text).text.toString()
            val worstSymptom = findViewById<EditText>(R.id.worst_symptom_edit_text).text.toString()
            val skinDiseaseHistory = findViewById<Spinner>(R.id.skin_disease_history_spinner).selectedItem.toString()
            val geneticSkinDisease = findViewById<Spinner>(R.id.genetic_skin_disease_spinner).selectedItem.toString()
            val skinMedicationHistory = findViewById<EditText>(R.id.skin_medication_history_edit_text).text.toString()
            val detailedDescription = findViewById<EditText>(R.id.detailed_description_edit_text).text.toString()

            if (ageText.isEmpty() || gender.isEmpty() || skinCondition.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua data!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = ageText.toIntOrNull()
            if (age == null || age <= 0) {
                Toast.makeText(this, "Usia harus berupa angka valid!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val skinHealthRequest = SkinHealthRequest(
                gender = gender,
                age = age,
                skinCondition = skinCondition,
                sensitiveSkin = sensitiveSkin,
                symptomStart = symptomStart,
                worstSymptom = worstSymptom,
                skinDiseaseHistory = skinDiseaseHistory,
                geneticSkinDisease = geneticSkinDisease,
                skinMedicationHistory = skinMedicationHistory,
                detailedDescription = detailedDescription
            )

            viewModel.email.observe(this) { email ->
                if (!email.isNullOrEmpty()) {
                    viewModel.submitSkinHealth(email, skinHealthRequest)
                    viewModel.submissionStatus.observe(this) { result ->
                        when (result) {
                            is Result.Loading -> {
                                progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                progressBar.visibility = View.GONE
                                Toast.makeText(this, "Data berhasil dikirim!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, ResultTestActivity::class.java).apply {
                                    putExtra("gender", gender)
                                    putExtra("age", age)
                                    putExtra("skinCondition", skinCondition)
                                    putExtra("sensitiveSkin", sensitiveSkin)
                                    putExtra("symptomStart", symptomStart)
                                    putExtra("worstSymptom", worstSymptom)
                                    putExtra("skinDiseaseHistory", skinDiseaseHistory)
                                    putExtra("geneticSkinDisease", geneticSkinDisease)
                                    putExtra("skinMedicationHistory", skinMedicationHistory)
                                    putExtra("detailedDescription", detailedDescription)
                                }
                                startActivity(intent)
                            }
                            is Result.Error -> {
                                Toast.makeText(this, "Gagal mengirim data: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Email tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val editTextDate = findViewById<EditText>(R.id.symptom_start_edit_text)
        editTextDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val formattedDate = "$year-${month + 1}-$dayOfMonth"
                    editTextDate.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
    }
}
