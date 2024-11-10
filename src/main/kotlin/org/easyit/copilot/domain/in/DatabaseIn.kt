package org.easyit.copilot.domain.`in`

data class DatabaseGenerateReqIn(
    val sql: String,
    val packageName: String,
    val entityName: String,
)