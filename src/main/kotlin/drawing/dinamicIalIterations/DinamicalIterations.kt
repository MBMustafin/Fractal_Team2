package drawing.dynamicalIterations

import androidx.compose.runtime.MutableState
import drawing.FractalPainter
import drawing.convertation.Plane


class DynamicalIterations(var fp: MutableState<FractalPainter>, var lastPlane: MutableState<Plane>) {
    private var lastXmin: Double = 0.0
    private var lastXmax: Double = 0.0
    private var lastYmin: Double = 0.0
    private var lastYmax: Double = 0.0
    private var lastPlaneSquare: Double = 0.5


    private var currentXmin: Double = 0.0
    private var currentXmax: Double = 0.0
    private var currentYmin: Double = 0.0
    private var currentYmax: Double = 0.0
    private var currentPlaneSquare: Double = 0.0

    var maxIterations = 0
    init {
        fp.value.plane?.let { plane ->
            currentXmin = plane.xMin
            currentXmax = plane.xMax
            currentYmin = plane.yMin
            currentYmax = plane.yMax
        }
        lastPlane.value.let { plane ->
            lastXmin = plane.xMin
            lastXmax = plane.xMax
            lastYmin = plane.yMin
            lastYmax = plane.yMax
        }
        maxIterations = fp.value.fractal.maxIterations
        currentPlaneSquare = (currentXmax - currentXmin) * (currentYmax - currentYmin)
        lastPlaneSquare = (lastXmax - lastXmin) * (lastYmax - lastYmin)
    }

    fun changeIterations(){
        val newMaxIterations = ( lastPlaneSquare/ currentPlaneSquare * 3000).toInt()
        fp.value.fractal.maxIterations += newMaxIterations
    }

    fun recreatingPlane() {
        fp.value.plane?.let {
            val temp = Plane(it.xMin, it.xMax, it.yMin, it.yMax, it.width, it.height)
            lastPlane.value = temp
        }
    }

}

fun turnDynamicIterations(checkedState: MutableState<Boolean>, fp: MutableState<FractalPainter>, lastPlane: MutableState<Plane>) {
    if (checkedState.value) {
        val temp = DynamicalIterations(fp, lastPlane)
        temp.changeIterations()
        temp.recreatingPlane()
        println(temp.maxIterations)
        fp.value.refresh = true
    }
}