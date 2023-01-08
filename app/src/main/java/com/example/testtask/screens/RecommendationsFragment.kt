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
            binding.motionLayout.transitionToState(R.id.readIt)
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
                        motionLayout.setTransition(R.id.start, R.id.unlike)
                        recommendationsViewModel.swipe()
                    }
                    R.id.offScreenReadIt -> {
                        motionLayout.progress = 0f
                        motionLayout.setTransition(R.id.start, R.id.unlike)
                        recommendationsViewModel.swipe()
                    }
                    R.id.offScreenLikeLast->{
                        motionLayout.progress = 0f
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

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                super.onTransitionChange(motionLayout, startId, endId, progress)
                when(startId){
                    R.id.like->{
                        if (progress==1f){
                            enableButtons(true)
                            profile.user?.let {it.likedBooksCnt++}
                            profile.user?.let { recommendationsViewModel.updateUser(it)}
                        }
                        else{
                            enableButtons(false)
                        }
                    }
                    R.id.unlike->{
                        if (progress==1f){
                            enableButtons(true)
                        }
                        else{
                            enableButtons(false)
                        }
                    }
                    R.id.wantToRead->{
                        if (progress==1f){
                            enableButtons(true)
                        }
                        else{
                            enableButtons(false)
                        }
                    }
                    R.id.readIt->{
                        if (progress==1f){
                            enableButtons(true)
                            val genre = binding.genre.text.toString()
                            profile.user?.let { user ->
                                user.readBooksCnt++
                                val cnt = user.genres.getOrElse(genre) {
                                    user.genres[genre] = 0
                                    0
                                }
                                user.genres[genre] = cnt+1
                                user.genres =  user.genres.toList().sortedBy { it.second }.asReversed().toMap().toMutableMap()
                                recommendationsViewModel.updateUser(user)
                            }
                        }
                        else{
                            enableButtons(false)
                        }
                    }
                    R.id.pass->{
                        if (progress==1f){
                            enableButtons(true)
                        }
                        else{
                            enableButtons(false)
                        }
                    }
                    R.id.likeLast->{
                        if (progress==1f){
                            enableButtons(true)
                            profile.user?.let {it.likedBooksCnt++}
                            profile.user?.let { recommendationsViewModel.updateUser(it)}
                        }
                        else{
                            enableButtons(false)
                        }
                    }
                    R.id.unlikeLast->{
                        if (progress==1f){
                            enableButtons(true)
                        }
                        else{
                            enableButtons(false)
                        }
                    }
                    else->{
                        enableButtons(true)
                    }

                }
            }
        })
    }

    private fun enableButtons(isEnabled:Boolean){
        if (isEnabled){
            binding.apply {
                likeButton.isEnabled = true
                dislikeButton.isEnabled = true
                readItButton.isEnabled = true
                resutButton.isEnabled = true
            }

        }
        else{
            binding.apply {
                likeButton.isEnabled = false
                dislikeButton.isEnabled = false
                readItButton.isEnabled = false
                resutButton.isEnabled = false
            }

        }
    }

    private fun bindCards(swipeModel: SwipeModel){

        if (swipeModel.topCard!=null){
            binding.apply {
                title.text = swipeModel.topCard!!.title
                authorFullName.text = swipeModel.topCard!!.author
                year.text = swipeModel.topCard!!.year.toString()
                genre.text = swipeModel.topCard!!.genre
                countOfPages.text = swipeModel.topCard!!.countOfPages.toString()
                descriptionText.text = swipeModel.topCard!!.description
            }

            Glide.with(requireActivity())
                .load(swipeModel.topCard!!.imageUrl)
                .placeholder(R.color.black)
                .into(binding.bookImage)
        }
        else{
            binding.apply {
                motionLayout.removeView(binding.topCard)
                likeButton.visibility = View.GONE
                dislikeButton.visibility = View.GONE
                readItButton.visibility = View.GONE
            }

        }
        if (swipeModel.bottomCard==null){
            binding.apply {
                motionLayout.setTransition(R.id.last, R.id.likeLast)
                motionLayout.transitionToState(R.id.last)
                motionLayout.removeView(binding.bottomCard)
            }

        }
        else {
            binding.apply {
                titleBottom.text = swipeModel.bottomCard!!.title
                authorFullNameBottom.text = swipeModel.bottomCard!!.author
                yearBottom.text = swipeModel.bottomCard!!.year.toString()
                genreBottom.text = swipeModel.bottomCard!!.genre
                countOfPagesBottom.text = swipeModel.bottomCard!!.countOfPages.toString()
                descriptionText.text = swipeModel.bottomCard!!.description
            }
            Glide.with(requireActivity())
                .load(swipeModel.bottomCard!!.imageUrl)
                .placeholder(R.color.white)
                .into(binding.bookImage)
        }
    }

    private fun updateUI(state: LoadingState){
        when (state) {
            LoadingState.loadingData -> {
                binding.apply {
                    progressBar.visibility = View.VISIBLE
                    motionLayout.visibility = View.GONE
                    likeButton.visibility = View.GONE
                    dislikeButton.visibility = View.GONE
                    resutButton.visibility = View.GONE
                    readItButton.visibility = View.GONE
                }

            }
            LoadingState.stopLoading -> {
                binding.apply {
                    progressBar.visibility = View.GONE
                    motionLayout.visibility = View.VISIBLE
                    likeButton.visibility = View.VISIBLE
                    dislikeButton.visibility = View.VISIBLE
                    resutButton.visibility = View.VISIBLE
                    readItButton.visibility = View.VISIBLE
                }

            }
            LoadingState.failLoading-> {
                binding.apply {
                    motionLayout.visibility  = View.GONE
                    progressBar.visibility = View.GONE
                    likeButton.visibility = View.GONE
                    dislikeButton.visibility = View.GONE
                    resutButton.visibility = View.GONE
                    readItButton.visibility = View.GONE
                }

                Toast.makeText(requireContext(),"Fail to load",Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newInstance() = RecommendationsFragment()
    }
}