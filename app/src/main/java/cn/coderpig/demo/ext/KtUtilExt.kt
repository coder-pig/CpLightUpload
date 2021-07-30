package cn.coderpig.demo.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: KT 常用扩展方法
 */
/*=== 链式调用简化IF-ELSE ===*/
inline infix fun Boolean.trueLet(trueBlock: Boolean.() -> Unit): Else {
    if (this) {
        trueBlock()
        return NotDoElse(this)
    }
    return DoElse(this)
}

inline infix fun Boolean.falseLet(falseBlock: Boolean.() -> Unit): Else {
    if (!this) {
        falseBlock()
        return NotDoElse(this)
    }
    return DoElse(this)
}

interface Else {
    infix fun elseLet(elseBlock: Boolean.() -> Unit)
}

class DoElse(private val boolean: Boolean) : Else {
    override infix fun elseLet(elseBlock: Boolean.() -> Unit) {
        elseBlock(boolean)
    }
}

class NotDoElse(private val boolean: Boolean) : Else {
    override infix fun elseLet(elseBlock: Boolean.() -> Unit) {}
}

/*=== Activity跳转扩展 ===*/
inline fun <reified T : Activity> Activity.fly(
    intent: Intent? = Intent(this, T::class.java),
    bundle: Bundle? = null,
    isFinish: Boolean? = false,
    isResolveActivity: Boolean? = false,
    vararg params: Pair<String, Any> = emptyArray()
) {
    params.forEach { intent!!.putExtraEx(it) }
    bundle?.let { intent!!.putExtras(bundle) }
    isResolveActivity!!
        .trueLet { intent!!.resolveActivity(this@fly.packageManager)?.also { startActivity(intent) } }
        .elseLet { startActivity(intent) }
    isFinish!!.trueLet { finish() }
}

inline fun <reified T : Activity> Activity.flyBack(
    intent: Intent? = Intent(this, T::class.java),
    bundle: Bundle? = null,
    requestCode: Int?,
    isResolveActivity: Boolean? = false,
    vararg params: Pair<String, Any> = emptyArray()
) {
    params.forEach { intent!!.putExtraEx(it) }
    bundle?.let { intent!!.putExtras(bundle) }
    isResolveActivity!!
        .trueLet { intent!!.resolveActivity(this@flyBack.packageManager)?.also { startActivityForResult(intent, requestCode!!) } }
        .elseLet { startActivityForResult(intent, requestCode!!) }
}

inline fun <reified T : Fragment> Fragment.flyBack(
    intent: Intent? = Intent(this.activity, T::class.java),
    bundle: Bundle? = null,
    requestCode: Int?,
    isResolveActivity: Boolean? = false,
    vararg params: Pair<String, Any> = emptyArray()
) {
    params.forEach { intent!!.putExtraEx(it) }
    bundle?.let { intent!!.putExtras(bundle) }
    isResolveActivity!!
        .trueLet { intent!!.resolveActivity(this@flyBack.activity!!.packageManager)?.also { startActivityForResult(intent, requestCode!!) } }
        .elseLet { startActivityForResult(intent, requestCode!!) }
}

fun Intent.putExtraEx(extra: Pair<String, Any>) {
    when (val data = extra.second) {
        is Array<*> -> {
            data.isArrayOf<Boolean>() trueLet { this@putExtraEx.putExtra(extra.first, data as BooleanArray) }
            data.isArrayOf<Byte>() trueLet { this@putExtraEx.putExtra(extra.first, data as ByteArray) }
            data.isArrayOf<Short>() trueLet { this@putExtraEx.putExtra(extra.first, data as ShortArray) }
            data.isArrayOf<Int>() trueLet { this@putExtraEx.putExtra(extra.first, data as IntArray) }
            data.isArrayOf<Long>() trueLet { this@putExtraEx.putExtra(extra.first, data as LongArray) }
            data.isArrayOf<Float>() trueLet { this@putExtraEx.putExtra(extra.first, data as FloatArray) }
            data.isArrayOf<Double>() trueLet { this@putExtraEx.putExtra(extra.first, data as DoubleArray) }
            data.isArrayOf<Char>() trueLet { this@putExtraEx.putExtra(extra.first, data as CharArray) }
            data.isArrayOf<Parcelable>() trueLet { this@putExtraEx.putExtra(extra.first, data as Array<out Parcelable>) }
            data.isArrayOf<CharSequence>() trueLet { this@putExtraEx.putExtra(extra.first, data as Array<out CharSequence>) }
            data.isArrayOf<String>() trueLet { this@putExtraEx.putExtra(extra.first, data as Array<out String>) }
        }
        is Boolean -> this.putExtra(extra.first, data)
        is Byte -> this.putExtra(extra.first, data)
        is Short -> this.putExtra(extra.first, data)
        is Int -> this.putExtra(extra.first, data)
        is Long -> this.putExtra(extra.first, data)
        is Float -> this.putExtra(extra.first, data)
        is FloatArray -> this.putExtra(extra.first, data)
        is Double -> this.putExtra(extra.first, data)
        is DoubleArray -> this.putExtra(extra.first, data)
        is Char -> this.putExtra(extra.first, data)
        is CharArray -> this.putExtra(extra.first, data)
        is CharSequence -> this.putExtra(extra.first, data)
        is String -> this.putExtra(extra.first, data)
        is Bundle -> this.putExtra(extra.first, data)
        is Parcelable -> this.putExtra(extra.first, data)
        is java.io.Serializable -> this.putExtra(extra.first, data)
    }
}

/*=== Fragment嵌套子Fragment替换 ===*/
fun Fragment.childReplace(content: Fragment, container: Int) {
    childFragmentManager.beginTransaction().replace(container, content).commitAllowingStateLoss()
}

/*=== 设置多个onClick ===*/
fun View.OnClickListener.setMulti(vararg params: View) {
    params.forEach { it.setOnClickListener(this) }
}

/*=== ArrayList添加可变个数参数 ===*/
fun <T> ArrayList<T>.addMulti(vararg params: T) {
    params.forEach { add(it) }
}

/*=== 时间戳处理 ===*/
@SuppressLint("SimpleDateFormat")
fun Long.handleDateStr(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = Date(this)
    val old: Date = sdf.parse(sdf.format(date))
    val now: Date = sdf.parse(sdf.format(Date()))
    val oldTime: Long = old.time
    val nowTime: Long = now.time
    val hour = (nowTime - oldTime) / (1000.0f * 60 * 60) // 小时差
    if (old.year != now.year) return SimpleDateFormat("yy年MM月dd日").format(date)
    return if (old.date == now.date) {
        SimpleDateFormat("HH:mm").format(date)
    } else {
        if (hour < 48) "昨天" else SimpleDateFormat("MM月dd日").format(date)
    }
}
