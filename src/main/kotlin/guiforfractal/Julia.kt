package guiforfractal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import drawing.FractalPainter
import drawing.SelectionRect
import drawing.colors.colors
import drawing.convertation.Converter
import drawing.convertation.Plane
import math.Complex
import math.fractals.Julia


@Composable
fun julia(selectedPoint: Complex, color: String){
    Julia.selectedPoint = selectedPoint

    val jp = remember {FractalPainter(Julia, colors[color]!!)}
    jp.plane = Plane(-2.0, 2.0, -2.0, 2.0, 0f, 0f)
    jp.plane?.let {
        jp.xMin = it.xMin
        jp.xMax = it.xMax
        jp.yMin = it.yMin
        jp.yMax = it.yMax
    }


    MaterialTheme {
        Canvas(Modifier.fillMaxSize().padding(8.dp)) {
            if(jp.width != size.width.toInt() || jp.height != size.height.toInt() ){
                jp.width = size.width.toInt()
                jp.height = size.height.toInt()
                jp.refresh = true

            }
            jp.paint(this)
            jp.scoping()
        }

        selectionPanel {
            jp.plane?.let{ plane ->
                val xMin = Converter.xScr2Crt(it.topLeft.x, plane)
                val xMax = Converter.xScr2Crt(it.topLeft.x+it.size.width, plane)
                val yMax = Converter.yScr2Crt(it.topLeft.y, plane)
                val yMin = Converter.yScr2Crt(it.topLeft.y+it.size.height, plane)

                plane.xMin = xMin
                plane.xMax = xMax
                plane.yMin = yMin
                plane.yMax = yMax

                jp.xMin = xMin
                jp.xMax = xMax
                jp.yMin = yMin
                jp.yMax = yMax
                jp.refresh = true
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun selectionPanel(
    onSelected: (SelectionRect)->Unit
) {
    var rect by remember {mutableStateOf(SelectionRect(Offset.Zero))}
    Canvas(Modifier.fillMaxSize().padding(8.dp).pointerInput(Unit){
        detectDragGestures(
            onDragStart = {
                rect = SelectionRect(it)
            },
            onDrag = {
                rect.addPoint(it)
            },
            onDragEnd = {
                onSelected(rect)
                rect = SelectionRect(Offset.Zero)
            },
            matcher = PointerMatcher.Primary)
    }){
        drawRect(Color(0f, 1f, 1f, 0.3f), rect.topLeft, rect.size)
    }
}


