package com.fahamin.notificationtask

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    var dayName: String = "MONDAY"
    var isLogging = true
    var workingTime: Int = 9

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workingTime = getTimeRightNow();
        dayName = getTodayName()
        getTimeRightNow()

        if (isLogging && dayName != "FRIDAY" && workingTime > 9 && workingTime <=18) {
            setWorker()
        }
    }

    fun getTimeRightNow(): Int {
        val currentTime: String = SimpleDateFormat("HH", Locale.getDefault()).format(Date())
        Log.e("currentTime", currentTime);
        return Integer.parseInt(currentTime)
    }

    fun getTodayName(): String {

        val calendar: Calendar = Calendar.getInstance()
        val day: Int = calendar.get(Calendar.DAY_OF_WEEK)

        when (day) {
            Calendar.SUNDAY -> {
                dayName = "SUNDAY"
            }

            Calendar.MONDAY -> {
                dayName = "MONDAY"

            }

            Calendar.TUESDAY -> {
                dayName = "TUESDAY"

            }

            Calendar.WEDNESDAY -> {
                dayName = "WEDNESDAY"
            }

            Calendar.SATURDAY -> {
                dayName = "SATURDAY"

            }

            Calendar.FRIDAY -> {
                dayName = "FRIDAY"

            }

            Calendar.THURSDAY -> {
                dayName = "THURSDAY"

            }
        }

        return dayName
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setWorker() {

        val channel =
            NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        val constrainst =
            androidx.work.Constraints.Builder().setRequiresBatteryNotLow(true)
                .build()
        val workRequest = PeriodicWorkRequest.Builder(
            NotificationWorker::class.java,
            15, TimeUnit.MINUTES
        ).setConstraints(constrainst).build()

        Log.i("WorkManager", workRequest.toString())

        WorkManager.getInstance(this).enqueue(workRequest)

    }
}
