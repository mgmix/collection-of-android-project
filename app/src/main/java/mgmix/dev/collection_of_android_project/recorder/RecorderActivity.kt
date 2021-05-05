package mgmix.dev.collection_of_android_project.recorder

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import mgmix.dev.collection_of_android_project.R

class RecorderActivity : AppCompatActivity() {

    private val recordButton: RecordButton by lazy { findViewById(R.id.recordButton) }
    private val resetButton: Button by lazy { findViewById(R.id.resetButton) }
    private val visualizerView: SoundVisualizerView by lazy { findViewById(R.id.soundVisualizer) }
    private val recordTimeTextView: CountUpTextView by lazy { findViewById(R.id.recordTimeTextView) }

    private var state = State.BEFORE_RECORDING
        set(value) {
            field = value
            resetButton.isEnabled = (value == State.AFTER_RECORDING) || (value == State.ON_PLAYING)
            recordButton.updateIconWithState(value)
        }
    private val requiredPermissions = arrayOf(Manifest.permission.RECORD_AUDIO)

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private val recordingFilePath by lazy { "${externalCacheDir?.absolutePath}/recording.3gp" }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recorder)
        actionBar.apply { title = "Recorder" }
        requestAudioPermission()
        initViews()
        bindViews()
        initVariables()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val audioRecordPermissionGranted =
            requestCode == REQUEST_RECORD_AUDIO_PERMISSION && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        if (!audioRecordPermissionGranted) finish()
    }

    private fun requestAudioPermission() {
        requestPermissions(requiredPermissions, REQUEST_RECORD_AUDIO_PERMISSION)
    }

    private fun initViews() {
        recordButton.updateIconWithState(state)
    }

    private fun bindViews() {
        visualizerView.onRequestCurrentAmplitude = {
            recorder?.maxAmplitude ?: 0
        }

        resetButton.setOnClickListener {
            stopPlaying()
            visualizerView.clearVisualization()
            recordTimeTextView.clearCountTime()
            state = State.BEFORE_RECORDING
        }

        recordButton.setOnClickListener {
            when (state) {
                State.BEFORE_RECORDING -> startRecording()
                State.ON_RECORDING -> stopRecording()
                State.AFTER_RECORDING -> startPlaying()
                State.ON_PLAYING -> stopPlaying()
            }
        }
    }

    private fun initVariables() {
        state = State.BEFORE_RECORDING
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(recordingFilePath)
            prepare()
        }
        recorder?.start()
        visualizerView.startVisualizing(false)
        recordTimeTextView.startCountUp()
        state = State.ON_RECORDING
    }

    private fun stopRecording() {
        recorder?.run {
            stop()
            release()
        }
        recorder = null
        visualizerView.stopVisualizing()
        recordTimeTextView.stopCountUp()
        state = State.AFTER_RECORDING
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            setDataSource(recordingFilePath)
            prepare()
        }
        player?.setOnCompletionListener {
            stopPlaying()
            state = State.AFTER_RECORDING
        }
        player?.start()
        visualizerView.startVisualizing(true)
        recordTimeTextView.startCountUp()
        state = State.ON_PLAYING
    }

    private fun stopPlaying() {
        player?.release()
        player = null
        visualizerView.stopVisualizing()
        recordTimeTextView.stopCountUp()
        state = State.AFTER_RECORDING
    }

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 201
    }

}