package com.example.terralysis.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss",
    Locale.US
).format(System.currentTimeMillis())

fun createTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun dateFormatter(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("GMT")
    val parsedDate = inputFormat.parse(date)

    val outputFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault())
    outputFormat.timeZone = TimeZone.getDefault()
    return outputFormat.format(parsedDate)
}

fun rotateImage(imagePath: String){
    val bitmap = BitmapFactory.decodeFile(imagePath)
    val exif = ExifInterface(imagePath)

    val orientation = exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_UNDEFINED
    )

    val matrix = Matrix()

    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
    }

    val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

    val rotatedOutputStream = FileOutputStream(imagePath)
    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, rotatedOutputStream)
    rotatedOutputStream.close()

}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}



