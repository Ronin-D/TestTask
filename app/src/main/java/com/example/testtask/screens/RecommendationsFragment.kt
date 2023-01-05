package com.example.testtask.screens

import android.opengl.Visibility
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.lifecycle.lifecycleScope
import com.example.testtask.R
import com.example.testtask.databinding.FragmentRecommendationsBinding
import com.example.testtask.models.SwipeModel
import com.example.testtask.models.User
import com.example.testtask.util.LoadingState
import kotlinx.coroutines.launch

class RecommendationsFragment : Fragment() {

    private lateinit var binding:FragmentRecommendationsBinding
    private val recommendationsViewModel: RecommendationsViewModel by lazy {
        ViewModelProvider(this)[RecommendationsViewModel::class.java]
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View{
        binding = FragmentRecommendationsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        recommendationsViewModel.books.observe(this){
            recommendationsViewModel.updateCards()
        }
        recommendationsViewModel.swipeModel.observe(viewLifecycleOwner){
            bindCards(it)
        }
        recommendationsViewModel.isDbEmpty().observe(viewLifecycleOwner){
            if (it){
                updateUI(LoadingState.loadingData)
                recommendationsViewModel.initDatabaseData()

            }
            else{
                updateUI(LoadingState.stopLoading)
            }
        }


        binding.motionLayout.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                when (currentId) {
                    R.id.offScreenUnlike,
                    R.id.offScreenLike -> {
                        motionLayout.progress = 0f
                        motionLayout.setTransition(R.id.start, R.id.unlike)
                        recommendationsViewModel.swipe()
                    }
                }
            }
        })
    }

    private fun bindCards(swipeModel: SwipeModel){

        if (swipeModel.topCard!=null){
            binding.topCard.setBackgroundColor(swipeModel.topCard!!.photo)
        }
        if (swipeModel.bottomCard==null){
            binding.motionLayout.setTransition(R.id.last, R.id.likeLast)
            binding.motionLayout.transitionToState(R.id.last)
            binding.motionLayout.removeView(binding.bottomCard)
        }
        else{
            binding.bottomCard.setBackgroundColor(swipeModel.bottomCard!!.photo)
        }
    }

    fun updateUI(state: LoadingState){
        if (state==LoadingState.loadingData){
            binding.progressBar.visibility = View.VISIBLE
            binding.motionLayout.visibility = View.GONE
        }
        else if (state==LoadingState.stopLoading){
            binding.progressBar.visibility = View.GONE
            binding.motionLayout.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance() = RecommendationsFragment()
    }
}