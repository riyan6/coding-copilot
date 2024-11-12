package org.easyit.copilot.service.impl

import org.easyit.copilot.domain.`in`.DatabaseGenerateReqIn
import org.easyit.copilot.service.DatabaseService
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class DatabaseServiceImplTest {

    @Test
    fun generate() {
        val sql = """
        CREATE TABLE `t_users` (
          `id` int unsigned NOT NULL AUTO_INCREMENT,
          `name` varchar(64) DEFAULT NULL COMMENT '名称',
          `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
          PRIMARY KEY (`id`)
        ) ENGINE=InnoDB AUTO_INCREMENT=10066994 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
    """.trimIndent()

        val databaseService: DatabaseService = DatabaseServiceImpl()

        // 构建
        val result = databaseService.generate(
            DatabaseGenerateReqIn(
                // 创建表语句
                sql,
                // 包名
                packageName = "com.xxx.projectA.domain.po.plan",
                // 实体类
                entityName = "MainPlan",
                // 模块名
                moduleName = "plan"
            )
        )

        println("实体类")
        println(result.entity.code)

        println("数据层")
        println(result.repository.code)

    }
}