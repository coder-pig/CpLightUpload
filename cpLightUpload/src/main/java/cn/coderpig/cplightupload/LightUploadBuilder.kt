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

    // 上传业务
    var uploads: Map<UploadTaskType, Upload?>? = null

    // 上传配置
    var configs: Map<UploadTaskType, LightUploadConfig>? = null


    fun addBeforeInterceptor(interceptor: Interceptor?): LightUploadBuilder {
        interceptor?.let { beforeInterceptors.add(it) }; return this
    }

    fun addDoneInterceptors(interceptor: Interceptor?): LightUploadBuilder {
        interceptor?.let { doneInterceptors.add(it) }; return this
    }

    fun upload(vararg uploads: Pair<UploadTaskType, Upload>): LightUploadBuilder {
        this.uploads = uploads.toMap(); return this
    }

    fun config(vararg configs: Pair<UploadTaskType, LightUploadConfig>): LightUploadBuilder {
        this.configs = configs.toMap(); return this
    }

}