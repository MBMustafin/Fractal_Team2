import androidx.compose.desktop.ui.tooling.preview.Preview
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import drawing.FractalPainter
import drawing.Painter
import drawing.SelectionRect
import drawing.convertation.Converter
import drawing.convertation.Plane
import math.fractals.Mandelbrot
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.log2
import kotlin.math.sin

@Composable
@Preview
fun App() {
    val fp = remember { FractalPainter(Mandelbrot){
        if (it == 1f) Color.Black
        else {
            val r = sin(it*15f).absoluteValue
            val g = (sin(-8f*it)* cos(it*5f+12f)).absoluteValue
            val b = log2(2f - cos(sin(18*-it)))
            Color(r, g, b)
        }
    }}
    fp.plane = Plane(-2.0, 1.0, -1.0, 1.0, 0f, 0f)
    MaterialTheme {
        DrawingPanel(fp){size ->
            fp.width = size.width.toInt()
            fp.height = size.height.toInt()
            fp.refresh = true
        }
        SelectionPanel{
            fp.plane?.let{ plane ->
                val xMin = Converter.xScr2Crt(it.topLeft.x, plane)
                var xMin_global by mutableStateOf(xMin)
                val xMax = Converter.xScr2Crt(it.topLeft.x+it.size.width, plane)
                var xMax_global by mutableStateOf(xMax)
                val yMax = Converter.yScr2Crt(it.topLeft.y, plane)
                val yMin = Converter.yScr2Crt(it.topLeft.y+it.size.height, plane)
                plane.xMin = xMin
                plane.xMax = xMax
                plane.yMin = yMin
                plane.yMax = yMax
                fp.refresh = true
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectionPanel(
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

@Composable
fun DrawingPanel(
    fp: Painter,
    onResize: (Size)-> Unit = {},
) {
    Canvas(Modifier.fillMaxSize().padding(8.dp)) {
        if(fp.width != size.width.toInt() || fp.height != size.height.toInt() )
            onResize(size)

        fp.paint(this)

    }
}


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Множество Мандельброта"
    ) {
        App()
    }
}
public fun takeScreenshot() {
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    print(screenSize)
    val screenRect = Rectangle(screenSize)
    val robot = Robot()
    val screenshot = robot.createScreenCapture(screenRect)

    val file = File("screenshot.png")
    ImageIO.write(screenshot, "png", file)
}