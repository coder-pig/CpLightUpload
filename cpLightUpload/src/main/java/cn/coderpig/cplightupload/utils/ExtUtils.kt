package cn.coderpig.cplightupload.utils

import android.util.Log
import cn.coderpig.cplightupload.*
import java.util.*

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 一些扩展方法
 */

/** 日志打印扩展 */
fun String.logV() = Log.v("CpLogger", this)
fun String.logD() = Log.d("CpLogger", this)
fun String.logW() = Log.w("CpLogger", this)
fun String.logE() = Log.e("CpLogger", this)

/** 获取不可变列表的扩展 */
fun <T> List<T>.immutableList(): List<T> = Collections.unmodifiableList(ArrayList(this))

/** 判断文件后缀返回对应Task */
fun generateTaskByPath(path: String?): Task? {
    if(path.isNullOrBlank()) return null
    path.split(".").let {
        if(it.size < 2) return null
        return when(it[1]) {
            "png", "jpg", "jpeg", "webp", "gif", "svg", "bmp" -> ImageTask()
            "mp4", "mov", "wmv", "flv", "avi", "mkv" -> VideoTask()
            "mp3", "wav", "aac", "flac", "ape", "alac" -> AudioTask()
            else -> ElseTask()
        }
    }
}