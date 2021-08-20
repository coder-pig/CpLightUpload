package cn.coderpig.cplightupload.upload

import cn.coderpig.cplightupload.LightUpload
import cn.coderpig.cplightupload.TaskStatus
import cn.coderpig.cplightupload.utils.logE
import cn.coderpig.cplightupload.utils.logV
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

/**
 * Author: zpj
 * Date: 2021-08-20
 * Desc: 封装自带抠脚 HttpUrlConnection 上传(默认实现)
 */
class HucUpload : Upload() {
    @Synchronized
    override fun sendRequest() {
        "开始文件上传...".logV()
        var ins: InputStream? = null
        try {
            mTask.reqData?.let { req ->
                val conn = (URL(req.uploadUrl).openConnection() as HttpURLConnection).apply {
                    readTimeout = req.timeout!!
                    connectTimeout = req.timeout!!
                    doInput = true
                    doOutput = true
                    useCaches = false
                    requestMethod = req.requestMethod
                    // 请求头设置
                    val boundary = UUID.randomUUID()
                    req.headers["Content-Type"] = "multipart/form-data;boundary=${boundary}"
                    for ((k, v) in req.headers) setRequestProperty(k, v)
                    val dos = DataOutputStream(outputStream)
                    val sb = StringBuilder().append("--").append(boundary).append("\r\n")
                        .append("Content-Disposition: form-data; name=\"file\"; filename=\"")
                        .append(mTask.md5).append(mTask.fileName).append("\"")
                        .append("\r\n")
                        .append("Content-Type: application/octet-stream; charset=utf-8")
                        .append("\r\n").append("\r\n")
                    dos.write(sb.toString().toByteArray())
                    ins = FileInputStream(File(mTask.filePath!!))
                    val bytes = ByteArray(1024)
                    var len: Int
                    while (ins!!.read(bytes).also { len = it } != -1) {
                        dos.write(bytes, 0, len)
                    }
                    ins!!.close()
                    dos.write("\r\n".toByteArray())
                    val endData: ByteArray = "--$boundary--\r\n".toByteArray()
                    dos.write(endData)
                    dos.flush()
                }
                // 获取响应
                val input = BufferedReader(InputStreamReader(conn.inputStream, "UTF-8"))
                val sb1 = StringBuilder()
                var ss: Int
                while (input.read().also { ss = it } != -1) {
                    sb1.append(ss.toChar())
                }
                val result = sb1.toString()
                "文件上传结束...".logV()
                mTask.response = Response(conn.responseCode, result)
                mTask.status = TaskStatus.DONE
                mCallback?.onSuccess(mTask)
            }
        } catch (e: IOException) {
            e.message?.logE()
            mTask.status = TaskStatus.FAILURE
            mTask.throwable = e
            mCallback?.onFailure(mTask)
            LightUpload.postTask(mTask)
        } finally {
            if (ins != null) {
                try {
                    ins!!.close()
                } catch (e: IOException) {
                    e.message?.logE()
                }
            }
        }
    }
}