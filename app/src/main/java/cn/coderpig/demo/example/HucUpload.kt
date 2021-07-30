package cn.coderpig.demo.example

import android.os.Build
import cn.coderpig.demo.task.Task
import cn.coderpig.demo.upload.IUpload
import cn.coderpig.demo.utils.logE
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc:
 */
class HucUpload : IUpload {
    private var mUrl: String? = null
    private var mTimeOut: Int = 10 * 1000
    private var mVersion: String = "5.9.1"
    private var mVersionName: String = "5.9.1"
    private var width: Int = 1080
    private var height: Int = 2116
    private var scale: Float = 2.75f
    private var md5: String? = null
    private var name: String? = null
    private var path: String? = null

    override fun initRequest(task: Task) {
        task.reqData?.let {
            mUrl = it.uploadUrl
            mTimeOut = it.timeout
            this.md5 = task.md5
            this.name = task.fileName
            this.path = task.filePath
        }
    }

    override fun sendRequest() {
        "上传图片".logE()
        var ins: InputStream? = null
        try {
            val boundary = UUID.randomUUID()
            val conn = (URL(mUrl).openConnection() as HttpURLConnection).apply {
                readTimeout = mTimeOut
                connectTimeout = mTimeOut
                doInput = true
                doOutput = true
                useCaches = false
                requestMethod = "POST" // 请求方式
                setRequestProperty("Charset", "utf-8") // 设置编码
                setRequestProperty("connection", "keep-alive")
                setRequestProperty("token", "6464637B676E66656764676E6663676366616465606E667B6F626361646467")
                setRequestProperty("Content-Type", "multipart/form-data;boundary=${boundary}")
                setRequestProperty("version", mVersion)
                setRequestProperty("User-Agent", "partner/${mVersionName}" +
                        "(Android;${Build.VERSION.RELEASE};${width}*${height};" +
                        "Scale=${scale};${Build.BRAND}=${Build.MODEL})")
                //                         当文件不为空时执行上传
                val dos = DataOutputStream(outputStream)
                val sb = StringBuilder().append("--").append(boundary).append("\r\n")
                    .append("Content-Disposition: form-data; name=\"file\"; filename=\"")
                    .append(md5).append(name).append("\"")
                    .append("\r\n").append("Content-Type: application/octet-stream; charset=utf-8")
                    .append("\r\n").append("\r\n")
                dos.write(sb.toString().toByteArray())
                ins = FileInputStream(File(path!!))
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
            val input = conn.inputStream
            val sb1 = StringBuilder()
            var ss: Int
            while (input.read().also { ss = it } != -1) {
                sb1.append(ss.toChar())
            }
            val result = sb1.toString()
            result.logE()
        } catch (e: IOException) {
            e.message?.logE()
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