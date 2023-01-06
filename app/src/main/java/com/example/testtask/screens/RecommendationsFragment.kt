package com.example.testtask.screens

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import com.example.testtask.R
import com.example.testtask.databinding.FragmentRecommendationsBinding
import com.example.testtask.models.SwipeModel
import com.example.testtask.models.User
import com.example.testtask.util.LoadingState
import com.example.testtask.util.Profile

class RecommendationsFragment : Fragment() {

    interface Callbacks{
        fun onStatistics()
    }

    private lateinit var binding:FragmentRecommendationsBinding
    private var callbacks:Callbacks? = null
    private val profile = Profile.get()
    private val recommendationsViewModel: RecommendationsViewModel by lazy {
        ViewModelProvider(this)[RecommendationsViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View{
        binding = FragmentRecommendationsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.resutButton.setOnClickListener {
            callbacks?.onStatistics()
        }

        recommendationsViewModel.books.observe(this){
            recommendationsViewModel.updateCards()
        }
        recommendationsViewModel.swipeModel.observe(viewLifecycleOwner){
            bindCards(it)
        }
        recommendationsViewModel.isDbEmpty().observe(viewLifecycleOwner){
            if (it){
                val user = User()
                updateUI(LoadingState.loadingData)
                recommendationsViewModel.initUser(user)

            }
            else{
                recommendationsViewModel.getUser().observe(this){ user->
                    if (user!=null){
                        if (profile.user==null){
                            profile.user = user
                        }
                        updateUI(LoadingState.stopLoading)
                    }
                }
            }
        }

        binding.readItButton.setOnClickListener {
            val genre = binding.genre.text.toString()
            profile.user?.let {
                it.readBooksCnt++
                val cnt = it.genres.getOrElse(genre){
                    it.genres[genre] = 0
                0}
                it.genres[genre] = cnt+1
               it.genres.entries.sortedBy { it.value }
                recommendationsViewModel.updateUser(it)
            }
            binding.motionLayout.transitionToState(R.id.like)

        }

        binding.motionLayout.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                when (currentId) {
                    R.id.offScreenUnlike->{
                        motionLayout.progress = 0f
                        motionLayout.setTransition(R.id.start, R.id.unlike)
                        recommendationsViewModel.swipe()
                    }
                    R.id.offScreenLike -> {
                        motionLayout.progress = 0f
                        profile.user?.let {it.likedBooksCnt++}
                        profile.user?.let { recommendationsViewModel.updateUser(it)}
                        motionLayout.setTransition(R.id.start, R.id.unlike)
                        recommendationsViewModel.swipe()
                    }
                    R.id.offScreenLikeLast->{
                        motionLayout.progress = 0f
                        profile.user?.let {it.likedBooksCnt++}
                        profile.user?.let { recommendationsViewModel.updateUser(it)}
                        motionLayout.setTransition(R.id.start, R.id.unlike)
                        recommendationsViewModel.swipe()
                    }
                    R.id.offScreenUnlikeLast->{
                        motionLayout.progress = 0f
                        motionLayout.setTransition(R.id.start, R.id.unlike)
                        recommendationsViewModel.swipe()
                    }
                    R.id.offScreenWantToRead->{
                        motionLayout.progress = 0f
                        motionLayout.setTransition(R.id.start, R.id.unlike)
                        recommendationsViewModel.swipe()
                    }
                    R.id.offScreenPass->{
                        motionLayout.progress = 0f
                        motionLayout.setTransition(R.id.start, R.id.unlike)
                        recommendationsViewModel.swipe()
                    }
                    R.id.offScreenWantToReadLast->{
                        motionLayout.progress = 0f
                        motionLayout.setTransition(R.id.start, R.id.unlike)
                        recommendationsViewModel.swipe()
                    }
                    R.id.offScreenPassLast->{
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
            binding.title.text = swipeModel.topCard!!.title
            binding.authorFullName.text = swipeModel.topCard!!.author
            binding.bookImage.setBackgroundColor(swipeModel.topCard!!.photo)
            binding.year.text = swipeModel.topCard!!.year.toString()
            binding.genre.text = swipeModel.topCard!!.genre
            binding.countOfPages.text = swipeModel.topCard!!.countOfPages.toString()
        }
        else{
            binding.motionLayout.removeView(binding.topCard)
            binding.likeButton.visibility = View.GONE
            binding.dislikeButton.visibility = View.GONE
            binding.readItButton.visibility = View.GONE
        }
        if (swipeModel.bottomCard==null){
            binding.motionLayout.setTransition(R.id.last, R.id.likeLast)
            binding.motionLayout.transitionToState(R.id.last)
            binding.motionLayout.removeView(binding.bottomCard)
        }
        else {
            binding.titleBottom.text = swipeModel.bottomCard!!.title
            binding.authorFullNameBottom.text = swipeModel.bottomCard!!.author
            binding.bookImageBottom.setBackgroundColor(swipeModel.bottomCard!!.photo)
            binding.yearBottom.text = swipeModel.bottomCard!!.year.toString()
            binding.genreBottom.text = swipeModel.bottomCard!!.genre
            binding.countOfPagesBottom.text = swipeModel.bottomCard!!.countOfPages.toString()
        }
    }

    private fun updateUI(state: LoadingState){
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