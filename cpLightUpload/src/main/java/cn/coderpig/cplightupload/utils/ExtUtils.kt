package cn.coderpig.cplightupload.utils

import android.util.Log

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
