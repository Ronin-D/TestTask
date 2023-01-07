package com.example.testtask.screens

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import com.bumptech.glide.Glide
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI(LoadingState.loadingData)
    }

    override fun onStart() {
        super.onStart()
        binding.resutButton.setOnClickListener {
            callbacks?.onStatistics()
        }

        binding.likeButton.setOnClickListener {
            binding.motionLayout.transitionToState(R.id.like)
        }

        binding.dislikeButton.setOnClickListener {
            binding.motionLayout.transitionToState(R.id.unlike)
        }

        recommendationsViewModel.books.observe(this){
            if (it!=null){
                updateUI(LoadingState.stopLoading)
                recommendationsViewModel.updateCards()
            }
            else{
                updateUI(LoadingState.failLoading)
            }

        }
        recommendationsViewModel.swipeModel.observe(viewLifecycleOwner){
            bindCards(it)
        }
        recommendationsViewModel.isDbEmpty().observe(viewLifecycleOwner){
            if (it){
                val user = User()
                recommendationsViewModel.initUser(user)

            }
            else{
                recommendationsViewModel.getUser().observe(this){ user->
                    if (user!=null){
                        if (profile.user==null){
                            profile.user = user
                        }
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
                        //do something
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
                        //do something
                        recommendationsViewModel.swipe()
                    }
                    R.id.offScreenPass->{
                        motionLayout.progress = 0f
                        motionLayout.setTransition(R.id.start, R.id.unlike)
                        //do something
                        recommendationsViewModel.swipe()
                    }
                    R.id.offScreenWantToReadLast->{
                        motionLayout.progress = 0f
                        motionLayout.setTransition(R.id.start, R.id.unlike)
                        //do something
                        recommendationsViewModel.swipe()
                    }
                    R.id.offScreenPassLast->{
                        motionLayout.progress = 0f
                        motionLayout.setTransition(R.id.start, R.id.unlike)
                        //do something
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
            Glide.with(requireActivity())
                .load(swipeModel.topCard!!.imageUrl)
                .placeholder(R.color.black)
                .into(binding.bookImage)
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
            Glide.with(requireActivity())
                .load(swipeModel.bottomCard!!.imageUrl)
                .placeholder(R.color.black)
                .into(binding.bookImage)
            binding.yearBottom.text = swipeModel.bottomCard!!.year.toString()
            binding.genreBottom.text = swipeModel.bottomCard!!.genre
            binding.countOfPagesBottom.text = swipeModel.bottomCard!!.countOfPages.toString()
        }
    }

    private fun updateUI(state: LoadingState){
        if (state==LoadingState.loadingData){
            binding.progressBar.visibility = View.VISIBLE
            binding.motionLayout.visibility = View.GONE
            binding.likeButton.visibility = View.GONE
            binding.dislikeButton.visibility = View.GONE
            binding.resutButton.visibility = View.GONE
            binding.readItButton.visibility = View.GONE
        }
        else if (state==LoadingState.stopLoading){
            binding.progressBar.visibility = View.GONE
            binding.motionLayout.visibility = View.VISIBLE
            binding.likeButton.visibility = View.VISIBLE
            binding.dislikeButton.visibility = View.VISIBLE
            binding.resutButton.visibility = View.VISIBLE
            binding.readItButton.visibility = View.VISIBLE
        }
        else{
            binding.motionLayout.visibility  = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.likeButton.visibility = View.GONE
            binding.dislikeButton.visibility = View.GONE
            binding.resutButton.visibility = View.GONE
            binding.readItButton.visibility = View.GONE
            Toast.makeText(requireContext(),"Fail",Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance() = RecommendationsFragment()
    }
}