package cn.coderpig.cplightupload.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.*
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 文件相关工具方法
 */
class FileUtils {
    companion object {
        private val HEX_DIGITS_UPPER = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

        /** 计算文件MD5，返回字符串 */
        fun getFileMD5ToString(file: File?): String? = bytes2HexString(getFileMD5(file))

        /** 计算文件MD5，返回字节数组 */
        private fun getFileMD5(file: File?): ByteArray? {
            if (file == null) return null
            var dis: DigestInputStream? = null
            try {
                val fis = FileInputStream(file)
                var md = MessageDigest.getInstance("MD5")
                dis = DigestInputStream(fis, md)
                val buffer = ByteArray(1024 * 256)
                while (true) {
                    if (dis.read(buffer) <= 0) break
                }
                md = dis.messageDigest
                return md.digest()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    dis?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return null
        }

        /** 字节数组转字符串 */
        private fun bytes2HexString(bytes: ByteArray?): String {
            if (bytes == null) return ""
            val hexDigits: CharArray = HEX_DIGITS_UPPER
            val len: Int = bytes.size
            if (len <= 0) return ""
            val ret = CharArray(len shl 1)
            var j = 0
            var i = 0
            while (i < len) {
                ret[j++] = hexDigits[bytes[i].toInt() shr 4 and 0x0f]
                ret[j++] = hexDigits[bytes[i].toInt() and 0x0f]
                i++
            }
            return String(ret)
        }

        /**
         * 读取相片exif信息中的旋转角度
         * [filePath] 图片路径
         * 返回角度
         * */
        fun readPictureDegree(filePath: String): Int {
            var degree = 0
            try {
                val exifInterface = ExifInterface(filePath)
                val orientation: Int = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL)
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return degree
        }

        /**
         * 图片旋转(相机)
         * */
        fun rotateToDegrees(filePath: String, degrees: Float) {
            try {
                var tempBitmap: Bitmap? = BitmapFactory.decodeFile(filePath)
                tempBitmap?.let {
                    tempBitmap = Bitmap.createBitmap(it, 0, 0, it.width, it.height,
                        Matrix().apply { reset(); setRotate(degrees) }, true)
                    saveBitmapFile(filePath, it)
                }
            } catch (e: OutOfMemoryError) {
                e.message?.let { msg -> msg.logE() }
            }
        }

        /** 保存Bitmap为图片文件 */
        private fun saveBitmapFile(filePath: String, bitmap: Bitmap) {
            val file = File(filePath) // 将要保存图片的路径
            try {
                val bos = BufferedOutputStream(FileOutputStream(file))
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                bos.flush()
                bos.close()
            } catch (e: IOException) {
                e.message?.let { msg -> msg.logE() }
            }
        }

        /**
         * 图片压缩 上传图片建议compress为30
         * */
        fun compressImage(bitmap: Bitmap?, file: File, compress: Int = 30) {
            if (bitmap == null) return
            try {
                val outStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, compress, outStream)
                outStream.flush()
                outStream.close()
            } catch (e: IOException) {
                e.message?.let { msg -> msg.logE() }
            }
        }

        fun getLocalImage(file: File): Bitmap? {
            val sWidth = 960
            val sHeight = 1440
            if (file.exists()) {
                try {
                    val bitmap = bitmapFromFile(file.path, sWidth, sHeight)
                    val width = bitmap.width
                    val height = bitmap.height
                    if (width <= sWidth) {
                        return bitmap
                    }
                    val newHeight = sWidth * height / width
                    val scaleWidth = sWidth.toFloat() / width
                    val scaleHeight = newHeight.toFloat() / height
                    val matrix = Matrix()
                    matrix.postScale(scaleWidth, scaleHeight)
                    return Bitmap.createBitmap(bitmap, 0, 0, width,
                        height, matrix, true)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                } catch (e: Error) {
                    System.gc()
                }
            }
            return null
        }

        private fun bitmapFromFile(pathName: String?, width: Int,
                                   height: Int): Bitmap {
            var reqWidth = width
            var reqHeight = height
            if (reqHeight == 0 || reqWidth == 0) {
                try {
                    return BitmapFactory.decodeFile(pathName)
                } catch (e: OutOfMemoryError) {
                    reqHeight = 40
                    reqWidth = 30
                }
            }
            var options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(pathName, options)
            options = calculateInSampleSize(options, reqWidth, reqHeight)
            return BitmapFactory.decodeFile(pathName, options)
        }


        /** 图片压缩处理（使用Options的方法） */
        private fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int,
            reqHeight: Int): BitmapFactory.Options {
            // 源图片的高度和宽度
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1
            if (height > reqHeight || width > reqWidth) {
                // 计算出实际宽高和目标宽高的比率
                val heightRatio = Math.round(height.toFloat()
                        / reqHeight.toFloat())
                val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
                // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
                // 一定都会大于等于目标的宽和高。
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            }
            // 设置压缩比例
            options.inSampleSize = inSampleSize
            options.inJustDecodeBounds = false
            return options
        }

    }
}