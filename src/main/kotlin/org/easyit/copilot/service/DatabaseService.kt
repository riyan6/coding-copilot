package org.easyit.copilot.service

import org.easyit.copilot.domain.`in`.DatabaseGenerateReqIn
import org.easyit.copilot.domain.out.GenerateResultOut

interface DatabaseService {

    fun generate(genIn: DatabaseGenerateReqIn): GenerateResultOut

}