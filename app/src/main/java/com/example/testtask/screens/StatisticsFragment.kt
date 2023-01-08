package com.example.testtask.screens

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testtask.R
import com.example.testtask.databinding.FragmentStatisticsBinding
import com.example.testtask.util.Profile

class StatisticsFragment : Fragment() {

interface Callbacks{
    fun onBack()
}

    private lateinit var  binding: FragmentStatisticsBinding
    private  var firstPlaceGenre:String? = null
    private  var secondPlaceGenre:String? = null
    private  var thirdPlaceGenre:String? = null
    private val user = Profile.get().user
    private var callbacks:Callbacks? = null


    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentStatisticsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    override fun onStart() {
        super.onStart()
        binding.backButton.setOnClickListener {
            callbacks?.onBack()
        }

        binding.shareButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT,
                getString
                    (
                    R.string.share_statistics_text,
                    firstPlaceGenre?:"Do not have yet",
                    secondPlaceGenre?:"Do not have yet",
                    thirdPlaceGenre?:"Do not have yet",
                    user?.readBooksCnt?:0,
                    user?.likedBooksCnt?:0)
                )
            }.also {
                startActivity(it)
            }
        }
    }

    private fun updateUI(){
        binding.countOfReadText.text = getString(
            R.string.count_of_read_text,user?.readBooksCnt?:0)
        binding.countOflikedBooksText.text = getString(
            R.string.count_of_liked_books_text,user?.likedBooksCnt?:0)
       user?.let {
           var cnt = 1
           for(elem in it.genres)
           {
                if (cnt==1){
                    firstPlaceGenre = elem.key
                    binding.firstPlaceText.text = getString(R.string.first_place_text,elem.key)
                }
               else if (cnt==2){
                    secondPlaceGenre = elem.key
                    binding.secondPlaceText.text = getString(R.string.second_place_text,elem.key)

               }
               else if (cnt==3){
                    thirdPlaceGenre = elem.key
                    binding.thirdPlaceText.text = getString(R.string.third_place_text,elem.key)
               }
               else{
                   break
               }
               cnt++
           }
       }
    }

    companion object {
        fun newInstance() = StatisticsFragment()
    }


}