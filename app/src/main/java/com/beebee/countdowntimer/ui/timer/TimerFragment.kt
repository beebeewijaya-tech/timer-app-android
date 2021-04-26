package com.beebee.countdowntimer.ui.timer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beebee.countdowntimer.databinding.FragmentTimerBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar

@AndroidEntryPoint
class TimerFragment : Fragment() {
    private lateinit var binding: FragmentTimerBinding

    @Inject
    lateinit var viewModel: TimerViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTimerBinding.inflate(inflater, container, false)

        binding.playButton.setOnClickListener {
            val hour = binding.spinnerTime.currentHour
            val minute = binding.spinnerTime.currentMinute

            val calendarNow = Calendar.getInstance()
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)

            val timer = calendar.timeInMillis - calendarNow.timeInMillis
            viewModel.startTimer(timer)
        }

        binding.pauseButton.setOnClickListener {
            viewModel.cancelTimer()
        }

        viewModel.isTimerSucceed.observe(viewLifecycleOwner, Observer {
            if (it) {
                Snackbar.make(binding.root, "Times up", Snackbar.LENGTH_SHORT)
                    .setAction("OK") { viewModel.clearTimerSucceed() }
                    .show()
            }
        })

        viewModel.isTimer.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.spinnerTime.visibility = View.GONE
                binding.playButton.visibility = View.GONE
                binding.pauseButton.visibility = View.VISIBLE
            } else {
                binding.spinnerTime.visibility = View.VISIBLE
                binding.pauseButton.visibility = View.GONE
                binding.playButton.visibility = View.VISIBLE
            }
        })

        viewModel.timer.observe(viewLifecycleOwner, Observer {
            binding.timer.text = "$it Second left"
        })

        return binding.root
    }
}