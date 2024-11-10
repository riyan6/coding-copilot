package org.easyit.copilot.domain.pojo

/**
 * 表信息
 */
data class TableInfo(
    val tableName: String,
    val columns: List<TableColumn>
)

/**
 * 表列
 */
data class TableColumn(
    val name: String,
    val type: String,
    var isPrimaryKey: Boolean = false,
    val comment: String?
)

