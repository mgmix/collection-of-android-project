package mgmix.dev.collection_of_android_project

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import mgmix.dev.collection_of_android_project.browser.BrowserActivity
import mgmix.dev.collection_of_android_project.calculator.CalculatorActivity
import mgmix.dev.collection_of_android_project.gallery.GalleryActivity
import mgmix.dev.collection_of_android_project.notification.NotificationActivity
import mgmix.dev.collection_of_android_project.pomodoro.PomodoroActivity
import mgmix.dev.collection_of_android_project.recorder.RecorderActivity
import mgmix.dev.collection_of_android_project.saying.SayingActivity
import mgmix.dev.collection_of_android_project.youtube.YoutubeActivity


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val listView: ViewGroup by lazy { findViewById(R.id.list) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addProject("Calculator", CalculatorActivity::class.java)
        addProject("Gallery", GalleryActivity::class.java)
        addProject("Pomodoro", PomodoroActivity::class.java)
        addProject("Recorder", RecorderActivity::class.java)
        addProject("Browser", BrowserActivity::class.java)
        addProject("Notification", NotificationActivity::class.java)
        addProject("Saying", SayingActivity::class.java)
        addProject("YoutubePlayer", YoutubeActivity::class.java)

    }


    private fun addProject(projectName: String, activity: Class<out Activity>) {
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        Button(this).apply {
            setLayoutParams(layoutParams)
            text = projectName
            tag = activity
            setOnClickListener(this@MainActivity)
            listView.addView(this)
        }
    }

    override fun onClick(v: View?) {
        v ?: return
        launchActivity(v.tag as Class<*>)
    }


}