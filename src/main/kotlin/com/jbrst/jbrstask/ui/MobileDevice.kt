package com.jbrst.jbrstask.ui

import com.jbrst.jbrstask.ui.Metrics.HEIGHT
import com.jbrst.jbrstask.ui.Metrics.PIXEL_RATIO
import com.jbrst.jbrstask.ui.Metrics.WIDTH


object Metrics {
    internal const val WIDTH = "width"
    internal const val HEIGHT = "height"
    internal const val PIXEL_RATIO = "pixelRatio"
}

enum class MobileDevice(
    val deviceMetrics: Map<String, Any>,
    val userAgent: String? = null
) {

    PIXEL7(
        mapOf(WIDTH to 1080, HEIGHT to 2400, PIXEL_RATIO to 2.22),
        "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Mobile Safari/537.36"
    ),
    PIXEL7PRO(
        mapOf(WIDTH to 1440, HEIGHT to 3120, PIXEL_RATIO to 2.16),
        "Mozilla/5.0 (Linux; Android 13; Pixel 7 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Mobile Safari/537.36"
    )

}