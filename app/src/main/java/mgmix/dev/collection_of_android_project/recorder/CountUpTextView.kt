package mgmix.dev.collection_of_android_project.recorder

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CountUpTextView(
    context: Context,
    attrs: AttributeSet
) : AppCompatTextView(context, attrs) {

    private var startTimeStamp: Long = 0L

    private val countUpAction: Runnable = object : Runnable {
        // 시작 시 타임스탬프를 찍고, 1초마다 반복 현재시간과 타임스탬프 비교 및 텍스트 뷰에 업데이트
        override fun run() {
            val currentTimeStamp = SystemClock.elapsedRealtime()
            val countTimeSecond = ((currentTimeStamp - startTimeStamp) / 1000L).toInt()
            updateCountTime(countTimeSecond)
            handler?.postDelayed(this, 1000L)
        }
    }

    fun startCountUp() {
        startTimeStamp = SystemClock.elapsedRealtime()
        handler?.post(countUpAction)
    }

    fun stopCountUp() {
        handler?.removeCallbacks(countUpAction)
    }

    private fun updateCountTime(countTimeSecond: Int) {
        val minutes = countTimeSecond / 60
        val seconds = countTimeSecond % 60

        text = "%02d:%02d".format(minutes, seconds)
    }

    fun clearCountTime() {
        updateCountTime(0)
    }

}