package drawing.convertation

data class FractalParameters(var width: Double, var height: Double, var scale: Double)

fun updateFractalParameters(oldParameters: FractalParameters, newWidth: Double, newHeight: Double, newScale: Double): FractalParameters {
    val widthRatio = newWidth / oldParameters.width
    val heightRatio = newHeight / oldParameters.height
    val scaleRatio = newScale / oldParameters.scale

    val updatedWidth = oldParameters.width * widthRatio
    val updatedHeight = oldParameters.height * heightRatio
    val updatedScale = oldParameters.scale * scaleRatio

    return FractalParameters(updatedWidth, updatedHeight, updatedScale)
}