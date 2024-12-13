package com.dicoding.picodiploma.loginwithanimation.data

data class SkinHealthRequest(
    val gender: String,
    val age: Int,
    val skinCondition: String,
    val sensitiveSkin: String,
    val symptomStart: String,
    val worstSymptom: String,
    val skinDiseaseHistory: String,
    val geneticSkinDisease: String,
    val skinMedicationHistory: String,
    val detailedDescription: String
)
