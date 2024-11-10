package org.easyit.copilot.util

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class DatabaseUtilTest {

    @Test
    fun parseCreateTableStatement() {
        val sql = """
        CREATE TABLE `t_users` (
          `id` int unsigned NOT NULL AUTO_INCREMENT,
          `name` varchar(64) DEFAULT NULL COMMENT '名称',
          `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
          PRIMARY KEY (`id`)
        ) ENGINE=InnoDB AUTO_INCREMENT=10066994 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
    """.trimIndent()

        val tableInfo = DatabaseUtil.parseCreateTableStatement(sql)
        println("Table Name: ${tableInfo?.tableName}")
        tableInfo?.columns?.forEach { column ->
            println("Column: ${column.name}, Type: ${column.type}, Primary Key: ${column.isPrimaryKey}, Comment: ${column.comment}")
        }
    }
}