package math.fractals
import math.Complex



class Julia : AlgebraicFractal {
    companion object {
        var selectedPoint = Complex(0.0,0.0)
    }

    override var maxIterations: Int = 2000
        private var r = 2.0
        val r2 = r * r
    override fun isInSet(c : Complex) : Float {
        var z: Complex  = c
        for(i in 0..maxIterations){
            z = z * z + selectedPoint
            if (z.abs2() >= r2)
                return i.toFloat() / maxIterations
        }
        return 1f
    }
}