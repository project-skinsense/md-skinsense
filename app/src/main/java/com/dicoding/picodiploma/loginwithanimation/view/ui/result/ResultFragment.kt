package com.dicoding.picodiploma.loginwithanimation.view.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.Prediction

class ResultFragment : Fragment() {

    companion object {
        fun newInstance() = ResultFragment()
    }

    private val viewModel: ResultViewModel by viewModels()

    private var prediction: Prediction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prediction = requireActivity().intent.getParcelableExtra("predict_result")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prediction?.let {
            val resultTextView: TextView = view.findViewById(R.id.result)
            resultTextView.text = it.result

            val explanationTextView: TextView = view.findViewById(R.id.explanation)
            explanationTextView.text = it.explanation

            val suggestionTextView: TextView = view.findViewById(R.id.suggesstion)
            suggestionTextView.text = it.suggestion

            val imageView: ImageView = view.findViewById(R.id.image_result)
            imageView.load(it.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.skin_logo)
                error(R.drawable.skin_logo)
            }
        }
    }
}
