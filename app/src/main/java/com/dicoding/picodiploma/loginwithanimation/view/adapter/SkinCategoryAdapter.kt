package com.dicoding.picodiploma.loginwithanimation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.loginwithanimation.data.Prediction
import com.dicoding.picodiploma.loginwithanimation.databinding.ItemSkinCategoryBinding
import com.squareup.picasso.Picasso

class SkinCategoryAdapter(
    private val categories: List<Prediction>,
    private val onItemClick: (Prediction) -> Unit
) : RecyclerView.Adapter<SkinCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSkinCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val prediction = categories[position]
        holder.bind(prediction, onItemClick)
    }

    override fun getItemCount(): Int = categories.size

    class ViewHolder(private val binding: ItemSkinCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(prediction: Prediction, onItemClick: (Prediction) -> Unit) {
            binding.textCategoryName.text = prediction.result
            binding.textCategoryDesc.text = prediction.explanation
            Picasso.get()
                .load(prediction.imageUrl)
                .into(binding.imageCategory)
            binding.root.setOnClickListener {
                onItemClick(prediction)
            }
        }
    }
}

