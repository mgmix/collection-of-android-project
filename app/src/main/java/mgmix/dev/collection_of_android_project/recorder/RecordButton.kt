package mgmix.dev.collection_of_android_project.recorder

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import mgmix.dev.collection_of_android_project.R

class RecordButton(
    context: Context,
    attrs: AttributeSet
) : AppCompatImageButton(context, attrs) {

    init {
        setBackgroundResource(R.drawable.shape_oval_button)
    }

    fun updateIconWithState(state: State) {
        when (state) {
            State.BEFORE_RECORDING -> {
                setImageResource(R.drawable.ic_record_24)
            }
            State.ON_RECORDING -> {
                setImageResource(R.drawable.ic_stop_24)
            }
            State.AFTER_RECORDING -> {
                setImageResource(R.drawable.ic_play_24)
            }
            State.ON_PLAYING -> {
                setImageResource(R.drawable.ic_stop_24)
            }
        }
    }

}