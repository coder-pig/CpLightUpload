package cn.coderpig.cplightupload.upload

import cn.coderpig.cplightupload.task.Task

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 上传接口
 */
interface IUpload {
    fun initRequest(task: Task)
    fun sendRequest()
}