package cn.coderpig.demo.ext

import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import cn.coderpig.demo.TestApp
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 文件操作相关扩展
 */
fun getExternalVideoPath(): String {
    return if (isExternalStorageDisable()) "" else getAbsolutePath(
        KotlinExtKit.context.getExternalFilesDir("Video")
    )
}

private fun isExternalStorageDisable(): Boolean {
    return Environment.MEDIA_MOUNTED != Environment.getExternalStorageState()
}

private fun getAbsolutePath(file: File?): String {
    return if (file == null) "" else file.absolutePath
}

/* 判断SD卡是否存在 */
fun isSdcardExits() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

fun getOutputMediaFileUri(): UriBean {
    val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Test")
    if (!mediaStorageDir.exists()) mediaStorageDir.mkdirs()
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val mediaFile = File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg")
    return UriBean(file2Uri(mediaFile), mediaFile.absolutePath)
}

data class UriBean(
    var uri: Uri,
    var path: String
)

fun file2Uri(file: File): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val authority = TestApp.context!!.packageName + ".provider"
        FileProvider.getUriForFile(TestApp.context!!, authority, file)
    } else {
        Uri.fromFile(file)
    }
}