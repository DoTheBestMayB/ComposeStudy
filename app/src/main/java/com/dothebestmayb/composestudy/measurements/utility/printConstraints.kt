package com.dothebestmayb.composestudy.measurements.utility

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout

fun Modifier.printConstraints(tag: String): Modifier {
    return layout { measureable, constraints ->
        println("$tag: $constraints")
        val placeable = measureable.measure(
            constraints = constraints
        )

        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}
