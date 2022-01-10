package app.swapartists.utilities.extension

import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.load
import coil.transform.RoundedCornersTransformation

private const val IMAGE_CORNER_RADIUS = 60f

fun ImageView.loadImage(uri: String?, @DrawableRes drawableResId: Int) {
    this.load(uri) {
        crossfade(true)
        placeholder(drawableResId)
        error(drawableResId)
        fallback(drawableResId)
        transformations(RoundedCornersTransformation(IMAGE_CORNER_RADIUS))
    }
}