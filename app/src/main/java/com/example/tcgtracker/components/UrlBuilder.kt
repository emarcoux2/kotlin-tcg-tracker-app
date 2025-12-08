package com.example.tcgtracker.components

import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.Quality
import net.tcgdex.sdk.models.SetResume

fun SetResume.logoUrl(
    quality: Quality = Quality.HIGH,
    ext: Extension = Extension.WEBP
): String? {
    return this.logo?.let { "$it/${quality.value}.${ext.value}" }
}