package mgmix.dev.collection_of_android_project.pomodoro

import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mgmix.dev.collection_of_android_project.R

class PomodoroActivity : AppCompatActivity() {

    private val remainMinutesTextView: TextView by lazy { findViewById(R.id.remainMinutesText) }
    private val remainSecondsTextView: TextView by lazy { findViewById(R.id.remainSecondsText) }
    private val seekBar: SeekBar by lazy { findViewById(R.id.seekBar) }
    private var currentCountDownTimer: CountDownTimer? = null

    private var tickingSoundId: Int? = null
    private var bellSoundId: Int? = null
    private val soundPool = SoundPool.Builder().build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pomodoro)
        actionBar.apply { title = "Pomodoro" }
        bindViews()
        initSounds()
    }

    override fun onResume() {
        super.onResume()
        soundPool.autoResume()
    }

    override fun onPause() {
        super.onPause()
        soundPool.autoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    private fun bindViews() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser)
                    updateRemainTime(progress * 60 * 1000L)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                stopCountDown()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar ?: return
                if (seekBar.progress == 0) stopCountDown() else startCountDown()
            }

        })
    }

    private fun createCountDownTimer(initialMillies: Long): CountDownTimer =
        object : CountDownTimer(initialMillies, 1000L) {
            override fun onTick(milliesUntilFinished: Long) {
                updateRemainTime(milliesUntilFinished)
                updateSeekBar(milliesUntilFinished)
            }

            override fun onFinish() {
                completeCountDown()
            }
        }

    private fun startCountDown() {
        currentCountDownTimer = createCountDownTimer(seekBar.progress * 60 * 1000L)
        currentCountDownTimer?.start()

        tickingSoundId?.let {
            soundPool.play(it, 1F, 1F, 0, -1, 1F)
        }
    }

    private fun completeCountDown() {
        updateRemainTime(0)
        updateSeekBar(0)

        soundPool.autoPause()
        bellSoundId?.let {
            soundPool.play(it, 1F, 1F, 0, 0, 1F)
        }
    }

    private fun stopCountDown() {
        currentCountDownTimer?.cancel()
        currentCountDownTimer = null
        soundPool.autoPause()
    }

    private fun updateRemainTime(remainMillies: Long) {
        val remainSeconds = remainMillies / 1000
        remainMinutesTextView.text = "%02d'".format(remainSeconds / 60)
        remainSecondsTextView.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekBar(remainMillies: Long) {
        seekBar.progress = (remainMillies / 1000 / 60).toInt()
    }

    private fun initSounds() {
        tickingSoundId = soundPool.load(this, R.raw.timer_ticking, 1)
        bellSoundId = soundPool.load(this, R.raw.timer_bell, 1)
    }

}