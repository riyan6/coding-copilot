package org.easyit.copilot.domain.out

data class GenerateResultOut(
    val entity: GenEntityOut,
    val repository: GenRepositoryOut
)

/**
 * 实体类生成结果
 */
data class GenEntityOut (
    /**
     * 实体类源码
     */
    val code: String,
    /**
     * 实体类文件名、类名
     */
    val fileName: String,
    /**
     * 实体类包名
     */
    val packageName: String,
)

data class GenRepositoryOut (
    /**
     * 实体类源码
     */
    val code: String,
    /**
     * 实体类文件名、类名
     */
    val fileName: String,
    /**
     * 实体类包名
     */
    val packageName: String,
)