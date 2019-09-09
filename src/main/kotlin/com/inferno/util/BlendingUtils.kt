package com.inferno.util

/**
 * Created by Fernando on 3/8/2017.
 */
object BlendingUtils {
    // Cubic Bezier Curves
    // See: https://en.wikipedia.org/wiki/Bézier_curve
    fun solveCubicBezier(t: Float, a: Float, b: Float, c: Float, d: Float): Float {
        return a * (1 - t) * (1 - t) * (1 - t) + b * 3f * (1 - t) * (1 - t) * t + c * 3f * (1 - t) * t * t + d * (t * t * t)
    }

    fun easeInExpo(t: Float): Float {
        return solveCubicBezier(t, 0.95f, 0.05f, 0.795f, 0.035f)
    }

    fun easeInQuartic(t: Float): Float {
        return solveCubicBezier(t, 0.755f, 0.05f, 0.855f, 0.06f)
    }

    fun linear(t: Float): Float {
        return t
    }
}
