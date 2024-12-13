package com.dicoding.picodiploma.loginwithanimation.view.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.FragmentProfileBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var nameTextView: TextView
    private lateinit var nameText: TextView
    private lateinit var emailTextView: TextView

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this,  ViewModelFactory.getInstance(requireContext())).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        nameText = binding.nameText
        nameTextView = binding.nameTextView
        emailTextView = binding.emailTextView

        profileViewModel.getUserName()
        profileViewModel.getUserEmail()

        profileViewModel.name.observe(viewLifecycleOwner, Observer { name ->
            nameTextView.text = name ?: "Name not available"
        })

        profileViewModel.name.observe(viewLifecycleOwner, Observer { name ->
            nameText.text = name ?: "Name not available"
        })

        profileViewModel.email.observe(viewLifecycleOwner, Observer { email ->
            emailTextView.text = email ?: "Email not available"
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}