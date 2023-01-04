package com.example.testtask

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testtask.databinding.FragmentRecommendationsBinding
import com.example.testtask.models.SwipeModel

class RecommendationsFragment : Fragment() {

    private lateinit var binding:FragmentRecommendationsBinding
    private lateinit var swipeAdapter:SwipeAdapter
    private val recommendationsViewModel:RecommendationsViewModel by lazy {
        ViewModelProvider(this)[RecommendationsViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View{
        binding = FragmentRecommendationsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeAdapter = SwipeAdapter(recommendationsViewModel)
    }

    override fun onStart() {
        super.onStart()
        recommendationsViewModel.books.observe(viewLifecycleOwner){
            recommendationsViewModel.updateCards()
        }
        recommendationsViewModel.swipeModel.observe(viewLifecycleOwner){
            bindCards(it)
        }
        binding.motionLayout.setTransitionListener(swipeAdapter)
    }

    fun bindCards(swipeModel: SwipeModel){
        if (swipeModel.bottomCard==null){
            binding.motionLayout.setTransition(R.id.last,R.id.likeLast)
            binding.motionLayout.transitionToState(R.id.last)
            binding.motionLayout.removeView(binding.bottomCard)
        }
        else{
            binding.bottomCard.setBackgroundColor(swipeModel.bottomCard!!.photo)
        }

        if (swipeModel.topCard!=null){
            binding.topCard.setBackgroundColor(swipeModel.topCard!!.photo)
        }
    }

    companion object {
        fun newInstance() = RecommendationsFragment()
    }
}