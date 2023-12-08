package math.fractals

import math.Complex

object Mandelbrot : AlgebraicFractal {
    override var maxIterations: Int = 5000
        set(value) { field = value.coerceIn(20..10000)}
    var r = 2.0
    override fun isInSet(c: Complex): Float {
        val z = Complex()
        val r2 = r*r
        for (i in 1..maxIterations){
            z*=z
            z+=c
            if (z.abs2() >= r2)
                return i.toFloat()/ maxIterations
        }
        return 1f
    }

}