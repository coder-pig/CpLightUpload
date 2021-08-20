package cn.coderpig.cplightupload.upload

import cn.coderpig.cplightupload.LightUpload
import cn.coderpig.cplightupload.Task
import cn.coderpig.cplightupload.utils.logV

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 上传接口(实现此接口，完成上传请求构造)
 */
abstract class Upload {
    protected lateinit var mTask: Task
    protected var mCallback: CallBack? = null

    // 初始化请求
    open fun initRequest(task: Task, callback: CallBack?) {
        this.mTask = task
        this.mCallback = callback
        "上传参数初始化...".logV()
        LightUpload.getConfig()?.get(mTask.taskType)?.reqData?.let { cReq ->
            // 请求参数覆盖，自定义Task优先级 > 默认配置
            mTask.reqData?.let { tReq ->
                if (tReq.uploadUrl.isNullOrBlank()) tReq.uploadUrl = cReq.uploadUrl
                if (tReq.requestMethod.isNullOrBlank()) tReq.requestMethod = cReq.requestMethod
                if (tReq.headers.isNullOrEmpty()) tReq.headers = cReq.headers
                if (tReq.params.isNullOrEmpty()) tReq.params = cReq.params
                if (tReq.data.isNullOrEmpty()) tReq.data = cReq.data
                if (tReq.timeout == null) tReq.timeout = cReq.timeout
            }
        }
    }

    abstract fun sendRequest()   // 执行请求

    // 回调接口
    interface CallBack {
        fun onSuccess(task: Task)
        fun onFailure(task: Task)
    }
}