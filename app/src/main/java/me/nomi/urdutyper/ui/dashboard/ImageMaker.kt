package me.nomi.urdutyper.ui.dashboard

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ImageMaker {
    private var pictureFile: File? = null
    private fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }

    fun saveBitMap(context: Context, drawView: View): Uri? {
        val pictureFileDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            "Nomiii"
        )
        if (!pictureFileDir.exists()) {
            val isDirectoryCreated = pictureFileDir.mkdirs()
            if (!isDirectoryCreated) Toast.makeText(
                context,
                "Failed to create the directory!",
                Toast.LENGTH_LONG
            ).show()
            return null
        }
        val formatter = SimpleDateFormat("yyyyMMdd_hhmmss", Locale.getDefault())
        val now = Date()
        val filename =
            pictureFileDir.path + File.separator + "UT" + formatter.format(now) + ".jpg"
        pictureFile = File(filename)
        val bitmap = getBitmapFromView(drawView)
        try {
            val oStream = FileOutputStream(pictureFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream)
            oStream.flush()
            oStream.close()
            Toast.makeText(context, "Saved: $filename", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "There was an error saving the image", Toast.LENGTH_LONG).show()
        }
        return FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            pictureFile!!
        )
    }

    @Throws(IOException::class)
    fun getImageFromImageView(context: Context, iv: ImageView, filename: String): Uri? {
        //to get the image from the ImageView (say iv)
        val draw = iv.drawable
        val bitmap = Bitmap.createBitmap(
            draw.intrinsicWidth,
            draw.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        draw.setBounds(0, 0, canvas.width, canvas.height)
        draw.draw(canvas)
        var outStream: FileOutputStream? = null
        val dir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            "Nomiii"
        )
        if (!dir.exists()) {
            val isDirectoryCreated = dir.mkdirs()
            if (!isDirectoryCreated) Toast.makeText(
                context,
                "Failed to create the directory!",
                Toast.LENGTH_LONG
            ).show()
            return null
        }
        val outFile = File(dir.path + File.separator + filename + ".jpg")
        outStream = FileOutputStream(outFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        outStream.flush()
        outStream.close()
        return FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            outFile
        )
    }
}