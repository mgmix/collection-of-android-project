package mgmix.dev.collection_of_android_project

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import mgmix.dev.collection_of_android_project.calculator.CalculatorActivity
import mgmix.dev.collection_of_android_project.gallery.GalleryActivity


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val listView: ViewGroup by lazy { findViewById(R.id.list) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addProject("Calculator", CalculatorActivity::class.java)
        addProject("Gallery", GalleryActivity::class.java)

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