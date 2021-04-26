package com.beebee.countdowntimer.ui.timer

import android.app.Activity
import android.app.NotificationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beebee.countdowntimer.utils.sendNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class TimerViewModel @Inject constructor(val activity: Activity) : ViewModel() {
    private var tickerChannel = ticker(1000, 0)
    private var timerCount = 0


    private val _timer = MutableLiveData<Int>()
    val timer: LiveData<Int>
        get() = _timer

    private val _isTimerSucceed = MutableLiveData<Boolean>()
    val isTimerSucceed: LiveData<Boolean>
        get() = _isTimerSucceed

    private val _isTimer = MutableLiveData<Boolean>(false)
    val isTimer: LiveData<Boolean>
        get() = _isTimer

    fun startTimer(time: Long) {
        if (time < 1) return
        tickerChannel = ticker(1000, 0)
        _isTimer.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            timerCount = time.toInt() / 1000

            for (event in tickerChannel) {
                if (timerCount < 1) {
                    _isTimerSucceed.postValue(true)
                    _isTimer.postValue(false)
                    tickerChannel.cancel()

                    val notificationManager = ContextCompat.getSystemService(activity, NotificationManager::class.java) as NotificationManager
                    notificationManager.sendNotification("Times up for your ${time.toInt() / 1000} Seconds timer", activity)
                    break
                }
                timerCount--
                _timer.postValue(timerCount)
                Timber.i("Hello ${_timer.value}")
            }
        }
    }

    fun clearTimerSucceed() {
        _isTimerSucceed.postValue(false)
    }

    fun cancelTimer() {
        tickerChannel.cancel()
        _timer.postValue(0)
        _isTimerSucceed.postValue(false)
        _isTimer.postValue(false)
        timerCount = 0
    }
}