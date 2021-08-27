package cn.coderpig.cplightupload

import cn.coderpig.cplightupload.upload.Upload


/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc:上传任务相关
 */

/** 后台返回信息简陋封装(响应体内容交给后续响应拦截器解析) */
data class Response(val statusCode: Int, val content: String)

/** 请求数据Entity */
data class ReqData(
    var uploadUrl: String? = null,   // 上传服务器地址
    var requestMethod: String? = "POST",  // 请求方式
    var reqHeaders: HashMap<String, String>? = null, // 请求头
    var resHeaders: HashMap<String, String>? = null, // 响应头
    var params: ArrayList<HashMap<String, String>>? = null, // GET请求参数
    var data: ArrayList<HashMap<String, String>>? = null, // POST请求参数
    var readTimeOut: Int? = 10 * 1000,   // 读取超时
    var connectTimeOut: Int? = 10 * 1000,   // 连接超时
    var writeTimeout: Int? = 20 * 1000   // 写入超时
)

/** 后台返回信息的简陋封装Entity (响应体内容交给后续响应拦截器解析) */
data class ResData(val statusCode: Int, val content: String)

/** 上传文件类型的枚举 */
enum class UploadTaskType { IMAGE, VIDEO, AUDIO, FILE, ELSE }

/** 上传任务状态枚举，依次为：上传前、上传中、上传失败、上传完成 */
enum class UploadTaskStatus { BEFORE, UPLOADING, FAILURE, DONE }

/** 上传任务基类 (抽取共有属性) */
abstract class Task {
    abstract var taskType: UploadTaskType
    var filePath: String? = null    // 文件路径
    var md5: String? = null         // 文件md5
    var fileName: String? = null    // 文件名
    var fileType: String? = null    // 文件类型
    var fileUrl: String? = null     // 上传后的图片URL
    var reqData: ReqData? = ReqData()    // 任务请求参数
    var callback: Upload.CallBack? = null   // 上传回调
    var response: ResData? = null  // 上传结果
    var throwable: Throwable? = null  // 异常信息
    var config: LightUploadConfig? = null   // 任务配置
    var status: UploadTaskStatus? = UploadTaskStatus.BEFORE  // 任务状态，初始化默认Before
}

/** 图片类型的上传任务 */
open class ImageTask : Task() {
    override var taskType = UploadTaskType.IMAGE
}

/** 视频类型的上传任务 */
open class VideoTask : Task() {
    override var taskType = UploadTaskType.VIDEO
}

/** 音频类型的上传任务 */
open class AudioTask : Task() {
    override var taskType = UploadTaskType.AUDIO
}

/** 文件类型的上传任务 */
open class FileTask : Task() {
    override var taskType = UploadTaskType.FILE
}


/** 其他类型的上传任务 */
open class ElseTask : Task() {
    override var taskType = UploadTaskType.ELSE
}
