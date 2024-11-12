package org.easyit.copilot.domain.`in`

data class DatabaseGenerateReqIn(
    /**
     * 建表语句
     */
    val sql: String,
    /**
     * 基础包名
     */
    val packageName: String,
    /**
     * 实体类名字
     */
    val entityName: String,
    /**
     * 模块名
     */
    val moduleName: String
)