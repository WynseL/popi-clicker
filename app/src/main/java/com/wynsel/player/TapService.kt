package com.wynsel.player

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.content.Intent
import android.graphics.Path
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi


class TapService: AccessibilityService() {

    companion object {
        val TAG = "TapService";
    }

    private lateinit var handler: Handler
    private var runnable: IntervalRunnable? = null
    private var delay = 1000L
    private var valueX = 0f
    private var valueY = 0f

    override fun onCreate() {
        super.onCreate()
        HandlerThread("auto-handler").also {
            it.start()
            handler = Handler(it.looper)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "START COMMAND")
        if (intent != null) {
            val action = intent.getStringExtra("action")
            when(action) {
                "play" -> {
                    delay = intent.getLongExtra("delay", 1000L)
                    valueX = intent.getFloatExtra("x", 0f)
                    valueY = intent.getFloatExtra("y", 0f)
                    if (runnable == null) {
                        runnable = IntervalRunnable()
                    }
                    handler.post(runnable!!)
                }
                "stop" -> {
                    handler.removeCallbacksAndMessages(null)
                }
                else  -> {}
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun playTap(x: Float, y: Float) {
        val swipePath = Path().apply {
            moveTo(x, y)
            lineTo(x, y)
        }
        val gestureBuilder = GestureDescription.Builder().apply {
            addStroke(StrokeDescription(swipePath, 0, 10))
        }
        dispatchGesture(gestureBuilder.build(), object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription) {
                super.onCompleted(gestureDescription)
                if (runnable != null) {
                    handler.postDelayed(runnable!!, delay)
                }
            }

            override fun onCancelled(gestureDescription: GestureDescription) {
                super.onCancelled(gestureDescription)
            }
        }, null)
    }

    override fun onInterrupt() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

    }

    inner class IntervalRunnable: Runnable {
        override fun run() {
            playTap(valueX, valueY)
        }
    }
}