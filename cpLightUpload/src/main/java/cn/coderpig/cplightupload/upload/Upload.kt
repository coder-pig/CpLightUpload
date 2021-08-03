package cn.coderpig.cplightupload.upload

import cn.coderpig.cplightupload.Task

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 上传接口(实现此接口，完成上传请求构造)
 */
abstract class Upload {
    protected var mCallback: CallBack? = null

    // 初始化请求
    open fun initRequest(task: Task, callback: CallBack?) {
        this.mCallback = callback
    }

    abstract fun sendRequest()   // 执行请求

    // 回调接口
    interface CallBack {
        fun onSuccess(task: Task)
        fun onFailure(task: Task)
    }
}