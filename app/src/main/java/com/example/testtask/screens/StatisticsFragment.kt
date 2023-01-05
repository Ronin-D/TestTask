package com.example.testtask.screens

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testtask.R
import com.example.testtask.databinding.FragmentStatisticsBinding
import com.example.testtask.util.Profile

class StatisticsFragment : Fragment() {


    private lateinit var viewModel: StatisticsViewModel
    private lateinit var  binding: FragmentStatisticsBinding
    private val user = Profile.get().user

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentStatisticsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        updateUI()
    }

    private fun updateUI(){
        //binding.countOfReadText.text = getString(R.string.count_of_read_text,profile.user.)
        binding.countOflikedBooksText.text = getString(R.string.count_of_liked_books_text,user?.likedBooksCnt?:0)

    }

    companion object {
        fun newInstance() = StatisticsFragment()
    }


}