package com.example.testtask

import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.lifecycle.ViewModel

class SwipeAdapter(private val viewModel: RecommendationsViewModel):TransitionAdapter() {
    override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
        when (currentId) {
            R.id.offScreenUnlike,
            R.id.offScreenLike -> {
                motionLayout.progress = 0f
                motionLayout.setTransition(R.id.start, R.id.like)
                viewModel.swipe()
            }
        }
    }

}