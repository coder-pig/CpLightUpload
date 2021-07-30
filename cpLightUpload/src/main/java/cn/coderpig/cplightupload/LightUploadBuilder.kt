package cn.coderpig.cplightupload

import cn.coderpig.cplightupload.interceptor.Interceptor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 上传相关配置(复杂的对象与它的表示进行分离)
 */
class LightUploadBuilder {
    companion object {
        private val DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool()  // 默认线程池
    }

    // 拦截器
    val beforeInterceptors: MutableList<Interceptor> = ArrayList()
    val readyInterceptors: MutableList<Interceptor> = ArrayList()
    val doneInterceptors: MutableList<Interceptor> = ArrayList()

    // 线程池
    var executorService: ExecutorService = DEFAULT_EXECUTOR_SERVICE

    fun addBeforeInterceptor(interceptor: Interceptor?): LightUploadBuilder {
        interceptor?.let { beforeInterceptors.add(it) }; return this
    }

    fun addReadyInterceptors(interceptor: Interceptor?): LightUploadBuilder {
        interceptor?.let { readyInterceptors.add(it) }; return this
    }

    fun addDoneInterceptors(interceptor: Interceptor?): LightUploadBuilder {
        interceptor?.let { doneInterceptors.add(it) }; return this
    }

    fun executorService(executorService: ExecutorService?): LightUploadBuilder {
        executorService?.let { this.executorService = executorService }; return this
    }


}