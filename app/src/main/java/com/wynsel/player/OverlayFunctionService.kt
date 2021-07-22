package com.wynsel.player

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.os.SystemClock
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.wynsel.player.drawer.TargetLineView
import com.wynsel.player.utils.Preference


class OverlayFunctionService: Service(), View.OnClickListener {

    companion object {
        private val TAG = "== OVERLAY =="

        const val DEVICE_WIDTH = "device_width"
        const val DEVICE_HEIGHT = "device_height"
    }

    private val preference by lazy { Preference.newInstance() }
    private val windowManagerParams by lazy {
        WindowManager.LayoutParams().apply {
        type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        format = PixelFormat.TRANSLUCENT
        flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        gravity = Gravity.START or Gravity.TOP
        width = WindowManager.LayoutParams.WRAP_CONTENT
        height = WindowManager.LayoutParams.WRAP_CONTENT
    } }

    private val windowManager by lazy { getSystemService(Context.WINDOW_SERVICE) as WindowManager }

    private var overlayView: View? = null
    private var gridView: TargetLineView? = null

    private var incrementButton: AppCompatImageButton? = null
    private var timerTextView: AppCompatTextView? = null
    private var decrementButton: AppCompatImageButton? = null

    private var incrementOffsetXButton: AppCompatImageButton? = null
    private var offsetXTextView: AppCompatTextView? = null
    private var decrementOffsetXButton: AppCompatImageButton? = null

    private var incrementOffsetYButton: AppCompatImageButton? = null
    private var offsetYTextView: AppCompatTextView? = null
    private var decrementOffsetYButton: AppCompatImageButton? = null

    private var closeButton: AppCompatImageButton? = null
    private var playStopButton: AppCompatImageButton? = null

    private var isHandlerRunning = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        startOverlay()
        startGrid()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (overlayView != null) {
            windowManager.removeView(overlayView)
            windowManager.removeView(gridView)
            overlayView = null
            gridView = null

            incrementButton = null
            decrementButton = null
            timerTextView = null
            closeButton = null
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id) {
                R.id.containerOverlay -> {

                }
                R.id.ibIncrementTimer -> {
                    preference.timerInMilli = preference.timerInMilli + 100
                    timerTextView?.text = "${preference.timerInMilli}ms"
                }
                R.id.ibDecrementTimer -> {
                    val currentMilli = preference.timerInMilli
                    if (currentMilli != 0) {
                        preference.timerInMilli = currentMilli - 100
                    }
                    timerTextView?.text = "${preference.timerInMilli}ms"
                }
                R.id.ibIncrementOffsetX -> {
                    preference.gridOffsetX = preference.gridOffsetX + 1
                    gridView?.leftOffset = preference.gridOffsetX
                    gridView?.postInvalidate()
                    offsetXTextView?.text = gridView?.leftOffset.toString()
                }
                R.id.ibDecrementOffsetX -> {
                    preference.gridOffsetX = preference.gridOffsetX - 1
                    gridView?.leftOffset = preference.gridOffsetX
                    gridView?.postInvalidate()
                    offsetXTextView?.text = gridView?.leftOffset.toString()
                }
                R.id.ibIncrementOffsetY -> {
                    preference.gridOffsetY = preference.gridOffsetY + 1
                    gridView?.topOffset = preference.gridOffsetY
                    gridView?.postInvalidate()
                    offsetYTextView?.text = gridView?.topOffset.toString()
                }
                R.id.ibDecrementOffsetY -> {
                    preference.gridOffsetY = preference.gridOffsetY - 1
                    gridView?.topOffset = preference.gridOffsetY
                    gridView?.postInvalidate()
                    offsetYTextView?.text = gridView?.topOffset.toString()
                }
                R.id.ibClose -> {
                    callTapService(false)
                    stopSelf()
                }
                R.id.ibPlayStop -> {
                    if (isHandlerRunning) {
                        playStopButton?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_circle_outline))
                        callTapService(false)
                    } else {
                        playStopButton?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_stop))
                        callTapService(true)
                    }
                    isHandlerRunning = !isHandlerRunning
                }
            }
        }
    }

    private fun startOverlay() {
        overlayView = View.inflate(this, R.layout.view_overlay_controls, null)

        incrementButton = overlayView?.findViewById<AppCompatImageButton>(R.id.ibIncrementTimer)?.apply { setOnClickListener(this@OverlayFunctionService) }
        timerTextView = overlayView?.findViewById(R.id.tvTimer)
        decrementButton = overlayView?.findViewById<AppCompatImageButton>(R.id.ibDecrementTimer)?.apply { setOnClickListener(this@OverlayFunctionService) }

        incrementOffsetXButton = overlayView?.findViewById<AppCompatImageButton>(R.id.ibIncrementOffsetX)?.apply { setOnClickListener(this@OverlayFunctionService) }
        offsetXTextView = overlayView?.findViewById(R.id.tvOffsetX)
        decrementOffsetXButton = overlayView?.findViewById<AppCompatImageButton>(R.id.ibDecrementOffsetX)?.apply { setOnClickListener(this@OverlayFunctionService) }

        incrementOffsetYButton = overlayView?.findViewById<AppCompatImageButton>(R.id.ibIncrementOffsetY)?.apply { setOnClickListener(this@OverlayFunctionService) }
        offsetYTextView = overlayView?.findViewById(R.id.tvOffsetY)
        decrementOffsetYButton = overlayView?.findViewById<AppCompatImageButton>(R.id.ibDecrementOffsetY)?.apply { setOnClickListener(this@OverlayFunctionService) }

        closeButton = overlayView?.findViewById<AppCompatImageButton>(R.id.ibClose)?.apply { setOnClickListener(this@OverlayFunctionService) }
        playStopButton = overlayView?.findViewById<AppCompatImageButton>(R.id.ibPlayStop)?.apply { setOnClickListener(this@OverlayFunctionService) }

        timerTextView?.text = "${preference.timerInMilli}ms"
        offsetXTextView?.text = preference.gridOffsetX.toString()
        offsetYTextView?.text = preference.gridOffsetY.toString()

        windowManager.addView(overlayView, windowManagerParams)
    }

    private fun startGrid() {
        gridView = TargetLineView(this).apply {
            width = currentDisplay.second
            height = currentDisplay.first

            topOffset = preference.gridOffsetY
            leftOffset = preference.gridOffsetX
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
    }

    private val currentDisplay: Pair<Float, Float>
        get() {
            val metrics = DisplayMetrics().also { windowManager.defaultDisplay.getMetrics(it) }
            return Pair(metrics.widthPixels.toFloat(), metrics.heightPixels.toFloat())
        }

    private fun callTapService(isEnabled: Boolean) {
        val intent = Intent(applicationContext, TapService::class.java)
        if (isEnabled) {
            val test = gridView?.topPoint ?: 0f to 0f
            intent.putExtra("action", "play")
            intent.putExtra("delay", preference.timerInMilli.toLong())
            intent.putExtra("x", test.first)
            intent.putExtra("y",test.second)
        } else {
            intent.putExtra("action", "stop")
        }
        application.startService(intent)
    }
}