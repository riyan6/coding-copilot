package org.easyit.copilot.domain.out

data class GenerateResultOut(
    val entity: GenEntityOut,
    val repository: GenRepositoryOut
)

data class GenEntityOut (
    val code: String,
    val fileName: String,
)

data class GenRepositoryOut (
    val code: String,
    val fileName: String,
)