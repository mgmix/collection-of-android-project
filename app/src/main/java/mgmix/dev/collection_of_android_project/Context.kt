package mgmix.dev.collection_of_android_project

import android.content.Context
import android.content.Intent

fun Context.launchActivity(activity: Class<*>) {
    startActivity(Intent(this, activity))
}