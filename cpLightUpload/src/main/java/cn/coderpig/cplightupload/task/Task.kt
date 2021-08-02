package cn.coderpig.cplightupload.task

import cn.coderpig.cplightupload.entity.ReqData
import cn.coderpig.cplightupload.entity.TaskStatus
import cn.coderpig.cplightupload.upload.Response
import cn.coderpig.cplightupload.upload.Upload


/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc:上传任务基类(抽取共有属性)
 */
abstract class Task {
    var md5: String? = null         // 文件md5
    var fileName: String? = null    // 文件名
    var fileType: String? = null    // 文件类型
    var filePath: String? = null    // 文件路径
    var fileUrl: String? = null     // 服务器文件URL
    var reqData: ReqData? = null    // 任务请求参数
    var status: TaskStatus? = TaskStatus.BEFORE  // 任务状态，初始化默认Before
    var callback: Upload.CallBack? = null   // 上传回调
    var response: Response? = null  // 上传结果
}