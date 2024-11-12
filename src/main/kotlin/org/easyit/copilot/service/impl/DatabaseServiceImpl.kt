package org.easyit.copilot.service.impl

import org.easyit.copilot.domain.`in`.DatabaseGenerateReqIn
import org.easyit.copilot.domain.out.GenerateResultOut
import org.easyit.copilot.service.DatabaseService
import org.easyit.copilot.util.DatabaseUtil
import org.easyit.copilot.util.GenEntityUtil
import org.easyit.copilot.util.GenRepositoryUtil
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class DatabaseServiceImpl : DatabaseService {

    override fun generate(genIn: DatabaseGenerateReqIn): GenerateResultOut {
        // 提取出表
        val tableInfo = DatabaseUtil.parseCreateTableStatement(genIn.sql)

        // 构建实体类
        val entity = GenEntityUtil.execute(tableInfo, genIn)

        // 构建数据层
        val repository = GenRepositoryUtil.execute(genIn, entity)

        return GenerateResultOut(
            entity = entity,
            repository = repository,
        )
    }


}