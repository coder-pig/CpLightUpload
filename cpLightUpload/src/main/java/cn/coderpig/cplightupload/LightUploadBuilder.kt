package cn.coderpig.cplightupload

import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.upload.Upload
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
    val doneInterceptors: MutableList<Interceptor> = ArrayList()

    // 线程池
    var executorService: ExecutorService = DEFAULT_EXECUTOR_SERVICE

    // 上传业务类
    var upload: Upload? = null

    // 上传配置
    var config: LightUploadConfig? = null

    fun addBeforeInterceptor(interceptor: Interceptor?): LightUploadBuilder {
        interceptor?.let { beforeInterceptors.add(it) }; return this
    }

    fun addDoneInterceptors(interceptor: Interceptor?): LightUploadBuilder {
        interceptor?.let { doneInterceptors.add(it) }; return this
    }

    fun executorService(executorService: ExecutorService?): LightUploadBuilder {
        executorService?.let { this.executorService = executorService }; return this
    }

    fun upload(upload: Upload?): LightUploadBuilder {
        this.upload = upload; return this
    }

    fun config(config: LightUploadConfig): LightUploadBuilder {
        this.config = config; return this
    }

}