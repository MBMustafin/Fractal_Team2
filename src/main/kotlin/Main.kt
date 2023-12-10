import guiforfractal.menu
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import drawing.FractalPainter
import drawing.colors.colors
import drawing.convertation.Plane
import math.fractals.Fractal
import java.awt.Dimension


@Composable
@Preview
fun App() {

    val fp = remember { mutableStateOf(FractalPainter(Fractal, colors["color1"]!!))}
    

    fp.value.plane = Plane(-2.0, 1.0, -1.0, 1.0, 0f, 0f)

    fp.value.plane?.let {
        fp.value.xMin = it.xMin
        fp.value.xMax = it.xMax
        fp.value.yMax = it.yMax
        fp.value.yMin = it.yMin
    }

    MaterialTheme {
        menu(fp)
    }
}

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Множество Мандельброта"
    ) {
        this.window.minimumSize = Dimension(600, 400)
        App()
    }
}