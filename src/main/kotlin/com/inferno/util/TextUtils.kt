package com.inferno.util

import com.morph.engine.math.Vector2f
import com.morph.engine.graphics.Color
import com.morph.engine.newgui.Element
import com.morph.engine.newgui.TextElement

object TextUtils {
    fun centerWithin(name: String, text: String, font: String, size: Int, color: Color, e: Element): TextElement {
        val textObj = TextElement(name,
                text,
                font,
                size,
                e.transform.position - (e.transform.scale * Vector2f(0.5f, 0.5f)),
                color,
                e.depth - 1)

        val textSize = (textObj.topRight!! - textObj.bottomLeft!!).abs()

        val shift = (e.transform.scale - textSize) * 0.5f
        textObj.transform.translate(shift)

        return textObj
    }
}