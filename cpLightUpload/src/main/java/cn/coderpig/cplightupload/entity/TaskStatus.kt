package cn.coderpig.cplightupload.entity

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 任务状态枚举
 * 依次为：上传前、准备上传、开始上传、上传中、上传后
 */
enum class TaskStatus {
    BEFORE, START, UPLOADING, FAILURE, DONE
}