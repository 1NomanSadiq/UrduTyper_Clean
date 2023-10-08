package me.nomi.urdutyper.presentation.utils.common

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object Fonts {
    fun copyFontToCache(context: Context, uri: Uri, fontName: String) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputFile = File(context.cacheDir, fontName)
            val outputStream = FileOutputStream(outputFile)

            inputStream?.copyTo(outputStream)

            inputStream?.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun getAllFontsFromCache(context: Context): List<Typeface> {
        val fonts: MutableList<Typeface> = ArrayList()
        val cacheDir: File = context.cacheDir
        val fontFiles = cacheDir.listFiles()

        if (fontFiles != null) {
            for (fontFile in fontFiles) {
                if (fontFile != null && fontFile.extension == "ttf") {
                    try {
                        val typeface: Typeface = Typeface.createFromFile(fontFile)
                        fonts.add(typeface)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        return fonts
    }
}
