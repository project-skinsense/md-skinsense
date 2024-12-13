package com.dicoding.picodiploma.loginwithanimation.data

import com.google.gson.annotations.SerializedName
import android.os.Parcel
import android.os.Parcelable

data class PredictResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("prediction")
    val prediction: Prediction?,

    @SerializedName("image")
    val image: Image?
)

data class Prediction(
    @SerializedName("id")
    val id: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("result")
    val result: String,

    @SerializedName("explanation")
    val explanation: String,

    @SerializedName("suggestion")
    val suggestion: String,

    @SerializedName("confidenceScore")
    val confidenceScore: Double,

    @SerializedName("imageUrl")
    val imageUrl: String,

    @SerializedName("createdAt")
    val createdAt: Any? // Ganti dengan tipe data yang sesuai jika formatnya diketahui
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readValue(Any::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(userId)
        parcel.writeString(result)
        parcel.writeString(explanation)
        parcel.writeString(suggestion)
        parcel.writeDouble(confidenceScore)
        parcel.writeString(imageUrl)
        parcel.writeValue(createdAt)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Prediction> {
        override fun createFromParcel(parcel: Parcel): Prediction {
            return Prediction(parcel)
        }

        override fun newArray(size: Int): Array<Prediction?> {
            return arrayOfNulls(size)
        }
    }
}


data class Image(
    @SerializedName("url")
    val url: String
)
