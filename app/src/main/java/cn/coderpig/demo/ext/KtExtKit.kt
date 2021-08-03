package cn.coderpig.demo.ext

import android.content.Context

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 入口类
 */
class KotlinExtKit private constructor() {
    companion object {
        lateinit var context: Context

        fun init(context: Context) {
            this.context = context
        }
    }

}