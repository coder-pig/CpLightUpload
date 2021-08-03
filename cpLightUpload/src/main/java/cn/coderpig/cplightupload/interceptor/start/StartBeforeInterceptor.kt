package cn.coderpig.cplightupload.interceptor.start

import cn.coderpig.cplightupload.LightUploadConstants
import cn.coderpig.cplightupload.LightUploadException
import cn.coderpig.cplightupload.Task
import cn.coderpig.cplightupload.TaskStatus
import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.utils.FileUtils
import cn.coderpig.cplightupload.utils.SPUtils
import cn.coderpig.cplightupload.utils.logV
import java.io.File

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 上传前第一个拦截器，可在此做一些常规操作
 */
class StartBeforeInterceptor : Interceptor {
    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Task {
        "============ 上传文件常规校验 ============".logV()
        val task = chain.task()
        "检查文件路径是为空...".logV()
        if (task.filePath.isNullOrBlank()) throw LightUploadException("The file path is null.")
        "检查文件是否存在...".logV()
        val uploadFile = File(task.filePath!!)
        if (!uploadFile.exists()) throw NoSuchFileException(file = uploadFile, reason = "The upload file doesn't exist.")
        "获取文件名...".logV()
        task.fileName = uploadFile.name
        "获取文件类型...".logV()
        val dot = task.fileName!!.lastIndexOf('.')
        task.fileType = if (dot > -1 && dot < task.fileName!!.length - 1) uploadFile.name.substring(dot + 1) else null
        "计算文件md5...".logV()
        task.md5 = FileUtils.getFileMD5ToString(uploadFile)
        if(task.config != null && task.config!!.enableQuickUpload) {
            "已启用快传功能，检测是否有历史上传记录".logV()
            SPUtils.getInstance(LightUploadConstants.quickUploadSP).get(task.md5!!).let {
                if(it.isNotBlank()) {
                    "查找到快传记录，不用上传 → $it".logV()
                    task.fileUrl = it
                    task.status = TaskStatus.DONE
                    return task
                } else {
                    "未找到快传记录，正常上传..."
                }
            }
        }
        "常规校验执行完毕 → ${task.fileName} → ${task.md5}".logV()
        return chain.proceed(task)
    }
}