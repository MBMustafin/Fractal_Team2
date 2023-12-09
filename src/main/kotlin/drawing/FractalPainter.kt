package drawing

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toComposeImageBitmap
import drawing.convertation.Converter
import drawing.convertation.Plane
import math.Complex
import math.fractals.AlgebraicFractal
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.concurrent.thread

class FractalPainter(
    val fractal: AlgebraicFractal,
    val colorFunc: (Float) -> Color = {if (it< 1f) Color.White else Color.Black}
) : Painter {
    var plane: Plane? = null
    override var width: Int
        get() = plane?.width?.toInt() ?: 0
        set(value) { plane?.width = value.toFloat() }
    override var height: Int
        get() = plane?.height?.toInt() ?: 0
        set(value) {plane?.height = value.toFloat()}

    var img = BufferedImage(
        1,
        1,
        BufferedImage.TYPE_INT_ARGB,
    )
    var refresh = true

    override fun paint(scope: DrawScope) {
        if (refresh) {
            refresh = false
            img = BufferedImage(
                scope.size.width.toInt(),
                scope.size.height.toInt(),
                BufferedImage.TYPE_INT_ARGB,
            )

            plane?.let { plane ->
                val tc = Runtime.getRuntime().availableProcessors()
                List(tc) { t ->
                    thread {
                        for (i in t..<width step tc)
                            for (j in 0..<height) {
                                val x = Complex(
                                    Converter.xScr2Crt(i.toFloat(), plane),
                                    Converter.yScr2Crt(j.toFloat(), plane)
                                )
                                img.setRGB(i, j, colorFunc(fractal.isInSet(x)).toArgb())
                            }
                    }
                }.forEach { it.join() }
            }
        }
        scope.drawImage(img.toComposeImageBitmap())
        val file = File("screenshot2.png")
        ImageIO.write(img, "png", file)
        val bufferedImage:BufferedImage  = ImageIO.read(File("screenshot2.png"));
        val newBufferedImage = BufferedImage(
            bufferedImage.width,
            bufferedImage.height, BufferedImage.TYPE_INT_RGB
        )
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, java.awt.Color.WHITE, null);
        ImageIO.write(newBufferedImage, "jpg", File("screen.jpg"));


    }
   }