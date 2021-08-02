package cn.coderpig.cplightupload.utils

import android.os.Looper

/**
 * Author: zpj
 * Date: 2021-08-02
 * Desc:
 */
interface MainThreadSupport {
    fun isMainThread(): Boolean
    fun createPoster(): MainPoster

    class AndroidHandlerMainThreadSupport(private val looper: Looper) : MainThreadSupport {

        override fun isMainThread() = looper == Looper.myLooper()

        override fun createPoster() = MainPoster(looper)
    }

}