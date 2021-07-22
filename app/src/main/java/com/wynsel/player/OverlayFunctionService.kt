package com.wynsel.player

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.os.SystemClock
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.appcompat.widget.AppCompatButton
import com.wynsel.player.drawer.TargetLineView


class OverlayFunctionService: Service(), View.OnClickListener {

    companion object {
        private val TAG = "== OVERLAY =="

        const val DEVICE_WIDTH = "device_width"
        const val DEVICE_HEIGHT = "device_height"
    }

    private val windowManager by lazy { getSystemService(Context.WINDOW_SERVICE) as WindowManager }

    private var overlayView: View? = null
    private var gridView: View? = null
    private var addButton: AppCompatButton? = null
    private var closeButton: AppCompatButton? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        overlayView = View.inflate(this, R.layout.view_overlay_controls, null)
        gridView = TargetLineView(this).apply {
            width = currentDisplay.second
            height = currentDisplay.first
        }
        addButton = overlayView?.findViewById<AppCompatButton>(R.id.btnAdd)?.apply {
            setOnClickListener(this@OverlayFunctionService)
        }
        closeButton = overlayView?.findViewById<AppCompatButton>(R.id.btnClose)?.apply {
            setOnClickListener(this@OverlayFunctionService)
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.START or Gravity.TOP
            x = 0
            y = 0
        }

        val gridParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        windowManager.addView(gridView, gridParams)
        windowManager.addView(overlayView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (overlayView != null) {
            windowManager.removeView(overlayView)
            windowManager.removeView(gridView)
            overlayView = null
            gridView = null
            addButton = null
            closeButton = null
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id) {
                R.id.containerOverlay -> {

                }
                R.id.btnAdd -> {

                }
                R.id.btnClose -> {
                    stopSelf()
                }
            }
        }
    }

    private val currentDisplay: Pair<Float, Float>
        get() {
            val metrics = DisplayMetrics().also { windowManager.defaultDisplay.getMetrics(it) }
            return Pair(metrics.widthPixels.toFloat(), metrics.heightPixels.toFloat())
        }

    private fun press() {
        val downTime = SystemClock.uptimeMillis()
        val eventTime = SystemClock.uptimeMillis() + (400 until 2000).random()
        val motionEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, 0f, 0f, 0)
        motionEvent.recycle()
    }
}