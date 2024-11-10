package org.easyit.copilot.util

import org.easyit.copilot.domain.pojo.TableColumn
import org.easyit.copilot.domain.pojo.TableInfo
import java.lang.RuntimeException

object DatabaseUtil {

    /**
     * 根据表创建语句得到表的表名和列
     */
    fun parseCreateTableStatement(sql: String): TableInfo {
        // 1. 提取表名
        val tableNameRegex = Regex("""CREATE\s+TABLE\s+`(\w+)`""")
        val tableNameMatch = tableNameRegex.find(sql)
        val tableName = tableNameMatch?.groupValues?.get(1) ?: throw RuntimeException("无法从SQL语句中解析表名。")

        // 2. 提取表定义内容
        val tableDefinitionRegex = Regex("""\`\w+\`\s*\(([\s\S]+?)\)\s*ENGINE=""")
        val tableDefinitionMatch = tableDefinitionRegex.find(sql)
        val tableDefinition = tableDefinitionMatch?.groupValues?.get(1) ?: throw RuntimeException("无法从SQL语句中解析表名。")

        // 3. 解析每一行的列信息
        val columns = tableDefinition.split(",\n").mapNotNull { line ->
            // 去除多余空格，并确认行是否符合列定义格式
            val trimmedLine = line.trim()
            if (!trimmedLine.startsWith("`")) return@mapNotNull null

            // 提取列名和类型
            val parts = trimmedLine.split("\\s+".toRegex(), limit = 3)
            val columnName = parts[0].removeSurrounding("`")
            val columnType = parts.getOrNull(1) ?: ""

            // 提取备注（如果有 COMMENT 字符串）
            val comment = if ("COMMENT" in trimmedLine) {
                trimmedLine.substringAfter("COMMENT '").substringBeforeLast("'")
            } else null

            TableColumn(name = columnName, type = columnType, comment = comment)
        }.toMutableList()

        // 4. 提取主键
        val primaryKeyRegex = Regex("""PRIMARY\s+KEY\s+\(`(\w+)`\)""")
        val primaryKeyMatch = primaryKeyRegex.find(sql)
        val primaryKeyColumn = primaryKeyMatch?.groupValues?.get(1)

        // 标记主键列
        columns.forEach { column ->
            if (column.name == primaryKeyColumn) {
                column.isPrimaryKey = true
            }
        }

        return TableInfo(tableName = tableName, columns = columns)
    }

}