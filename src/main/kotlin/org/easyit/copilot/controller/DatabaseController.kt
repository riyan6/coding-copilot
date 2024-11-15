package org.easyit.copilot.controller

import org.easyit.copilot.domain.`in`.DatabaseGenerateReqIn
import org.easyit.copilot.domain.out.GenerateResultOut
import org.easyit.copilot.service.DatabaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping("/api/databases")
class DatabaseController @Autowired constructor(
    val databaseService: DatabaseService
) {

    @PostMapping("/generate")
    fun parseCreateTableStatementSql(@RequestBody genIn: DatabaseGenerateReqIn): GenerateResultOut {
        return databaseService.generate(genIn)
    }

}