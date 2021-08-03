package cn.coderpig.cplightupload.utils

import android.content.Context
import android.content.SharedPreferences
import cn.coderpig.cplightupload.LightUpload
import java.util.*

/**
 * Author: zpj
 * Date: 2021-08-03
 * Desc: SharedPreferences工具类
 */
class SPUtils {
    companion object {
        private val SP_UTILS_MAP: HashMap<String, SPUtils> = HashMap()

        fun getInstance() = getInstance("", Context.MODE_PRIVATE)

        fun getInstance(name: String) = getInstance(name, Context.MODE_PRIVATE)

        fun getInstance(mode: Int) = getInstance("", mode)

        fun getInstance(name: String, mode: Int): SPUtils {
            val spName = if (name.isNotBlank()) name else "spUtils"
            var spUtils = SP_UTILS_MAP[spName]
            if (spUtils == null) {
                synchronized(SPUtils::class.java) {
                    spUtils = SP_UTILS_MAP[spName]
                    if (spUtils == null) {
                        spUtils = SPUtils(spName, mode)
                        SP_UTILS_MAP[spName] = spUtils!!
                    }
                }
            }
            return spUtils!!
        }
    }

    private var sp: SharedPreferences? = null


    private constructor(spName: String) {
        sp = LightUpload.getContext()?.getSharedPreferences(spName, Context.MODE_PRIVATE)
    }

    private constructor(spName: String, mode: Int) {
        sp = LightUpload.getContext()?.getSharedPreferences(spName, mode)
    }

    fun put(key: String, value: String) {
        sp!!.edit().putString(key, value).apply()
    }

    fun get(key: String) = sp!!.getString(key, "")!!


}