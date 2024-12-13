package com.dicoding.picodiploma.loginwithanimation.view.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.FragmentHomeBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.adapter.SkinCategoryAdapter
import com.google.android.material.button.MaterialButton
import androidx.navigation.fragment.findNavController
import com.dicoding.picodiploma.loginwithanimation.view.detail.DetailActivity
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var iconTextButton: MaterialButton
    private lateinit var btnLogout: ImageButton
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireContext())
        ).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 1)

        iconTextButton = binding.iconTextButton
        iconTextButton.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.navigation_scan)
            (activity as MainActivity).updateBottomNavigationToScan()
        }

        btnLogout = binding.actionLogout
        btnLogout.setOnClickListener {
            homeViewModel.logout()
            Toast.makeText(requireContext(), "You Have Been Logged Out", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        progressBar = binding.progressBar

        homeViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        observeViewModel()

        return root
    }

    private fun observeViewModel() {
        homeViewModel.email.observe(viewLifecycleOwner) { email ->
            if (!email.isNullOrEmpty()) {
                homeViewModel.getPredictions(email)
            } else {
                Toast.makeText(requireContext(), "Email not found. Please login again.", Toast.LENGTH_SHORT).show()
            }
        }

        homeViewModel.predictions.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { predictions ->
                val adapter = SkinCategoryAdapter(predictions) { prediction ->
                    val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                        putExtra(DetailActivity.EXTRA_RESULT, prediction.result)
                        putExtra(DetailActivity.EXTRA_IMAGE_URL, prediction.imageUrl)
                        putExtra(DetailActivity.EXTRA_DESCRIPTION, prediction.explanation)
                        putExtra(DetailActivity.EXTRA_PREVENTION, prediction.suggestion)
                        putExtra(DetailActivity.EXTRA_SCORE, prediction.confidenceScore.toString())
                    }
                    startActivity(intent)
                }
                binding.recyclerView.adapter = adapter
            }, onFailure = { error ->
                Log.e("HomeFragment", "Error loading predictions: ${error.message}")
                Toast.makeText(requireContext(), "Failed to load predictions: ${error.message}", Toast.LENGTH_SHORT).show()
            })
        }

        homeViewModel.getUserEmail()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

