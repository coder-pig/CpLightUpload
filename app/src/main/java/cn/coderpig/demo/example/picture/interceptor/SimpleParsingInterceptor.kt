package cn.coderpig.demo.example.picture.interceptor

import cn.coderpig.cplightupload.ImageTask
import cn.coderpig.cplightupload.Task
import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.utils.logD
import cn.coderpig.cplightupload.utils.logV
import org.json.JSONObject

/**
 * Author: zpj
 * Date: 2021-08-02
 * Desc: 抠脚http解析器
 */
class SimpleParsingInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Task {
        val task = chain.task()
        if(task is ImageTask) {
            task.response?.let {
                var tempContent = it.content
                if(tempContent.startsWith("{")) {
                    val index: Int = tempContent.indexOf("{")
                    tempContent = tempContent.substring(index)
                }
                try {
                    val jsonObject = JSONObject(tempContent)
                    if (jsonObject.getInt("code") == 200) {
                        //解析 服务端回传内容
                        val mapJson: JSONObject = jsonObject.getJSONObject("data")
                        var key = ""
                        var image = ""
                        val ite = mapJson.keys()
                        while (ite.hasNext()) {
                            key = ite.next()
                            image = mapJson[key] as String
                        }
                        task.fileUrl = image
                        task.fileUrl?.logV()
                    } else {
                        jsonObject.toString().logV()
                    }
                } catch (e: Exception) {
                    e.message?.logD()
                }
            }
        }
        return chain.proceed(task)
    }
}