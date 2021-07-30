package cn.coderpig.cplightupload.interceptor

import cn.coderpig.cplightupload.task.Task
import java.io.IOException

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 拦截器基类(责任链递归方式传递)
 */
interface Interceptor {
    @Throws(IOException::class)
    fun intercept(chain: Chain): Task

    interface Chain {
        fun task(): Task

        @Throws(IOException::class)
        fun proceed(task: Task): Task
    }
}