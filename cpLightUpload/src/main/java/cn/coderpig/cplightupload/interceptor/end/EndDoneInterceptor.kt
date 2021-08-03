package cn.coderpig.cplightupload.interceptor.end

import cn.coderpig.cplightupload.LightUpload
import cn.coderpig.cplightupload.LightUploadConstants
import cn.coderpig.cplightupload.Task
import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.utils.SPUtils
import cn.coderpig.cplightupload.utils.logV

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 上传完成最后一个拦截器
 */
class EndDoneInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Task {
        "============ 上传任务执行完毕 ============ ".logV()
        val task = chain.task()
        if(task.md5 != null && !task.fileUrl.isNullOrBlank()) {
            SPUtils.getInstance(LightUploadConstants.quickUploadSP).put(task.md5!!, task.fileUrl!!)
        }
        LightUpload.postTask(task)
        return chain.task()
    }
}