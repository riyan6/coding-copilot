package org.easyit.copilot.util

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import org.easyit.copilot.domain.`in`.DatabaseGenerateReqIn
import org.easyit.copilot.domain.out.GenEntityOut
import org.easyit.copilot.domain.pojo.TableInfo
import java.lang.reflect.Array
import java.lang.reflect.Type
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import javax.lang.model.element.Modifier

object GenEntityUtil {

    /**
     * 构建实体类代码
     */
    fun execute(tableInfo: TableInfo, reqIn: DatabaseGenerateReqIn): GenEntityOut {
        // 添加 Lombok 注解
        val dataAnnotation = AnnotationSpec.builder(ClassName.get("lombok", "Data")).build()
        val allArgsConstructor = AnnotationSpec.builder(ClassName.get("lombok", "AllArgsConstructor")).build()
        val noArgsConstructor = AnnotationSpec.builder(ClassName.get("lombok", "NoArgsConstructor")).build()
        val builderAnnotation = AnnotationSpec.builder(ClassName.get("lombok", "Builder")).build()
        val tableNameAnnotation = AnnotationSpec.builder(ClassName.get("com.baomidou.mybatisplus.annotation", "TableName"))
            .addMember("value", "\$S", tableInfo.tableName)
            .addMember("excludeProperty", "\$L", """{"isused", "isdel"}""")
            .build()

        // 构建类
        val entityClass = TypeSpec.classBuilder(reqIn.entityName)
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(dataAnnotation)
            .addAnnotation(allArgsConstructor)
            .addAnnotation(noArgsConstructor)
            .addAnnotation(builderAnnotation)
            .addAnnotation(tableNameAnnotation)
            .superclass(ClassName.get("com.yunjin.framework.support.domain.po", "BasePo"))

        tableInfo.columns
            .filter { checkFieldValidity(it.name) }
            .forEach {
                val fieldSpec = FieldSpec.builder(checkFieldType(it.type), it.name)
                    .addModifiers(Modifier.PRIVATE)

                // javadoc
                fieldSpec.addJavadoc("<p>\$L</p>", it.comment ?: "")

                // 主键
                if (it.isPrimaryKey) {
                    val idType = ClassName.get("com.baomidou.mybatisplus.annotation", "IdType")
                    fieldSpec.addAnnotation(
                        AnnotationSpec.builder(ClassName.get("com.baomidou.mybatisplus.annotation", "TableId"))
                            .addMember("type", "\$T.\$L", idType, "ASSIGN_ID")
                            .build()
                    )
                    fieldSpec.addAnnotation(
                        AnnotationSpec.builder(ClassName.get("io.swagger.annotations", "ApiModelProperty"))
                            .addMember("value", "\$S", "主键")
                            .build()
                    )
                } else {
                    // 非主键
                    fieldSpec.addAnnotation(
                        AnnotationSpec.builder(ClassName.get("com.baomidou.mybatisplus.annotation", "TableField"))
                            .addMember("value", "\$S", it.name)
                            .build()
                    )
                    fieldSpec.addAnnotation(
                        AnnotationSpec.builder(ClassName.get("io.swagger.annotations", "ApiModelProperty"))
                            .addMember("value", "\$S", it.comment)
                            .build()
                    )
                }

                // 添加属性
                entityClass.addField(fieldSpec.build())
            }

        // 实体类名
        val entityPackageName = "${reqIn.packageName}.domain.po.${reqIn.moduleName}"

        // 生成 Java 文件
        val javaFile = JavaFile.builder(entityPackageName, entityClass.build())
            .build()

        return GenEntityOut(
            code = javaFile.toString(),
            fileName = "${reqIn.entityName}.java",
            packageName = entityPackageName
        )
    }

    /**
     * 检测某些列是否需要生成
     *
     * @param fieldName 属性名
     * @return true可以生成 false不能生成
     */
    private fun checkFieldValidity(fieldName: String): Boolean {
        return when (fieldName.lowercase().trim()) {
            "creator" -> false
            "creationdate" -> false
            "lastoperator" -> false
            "lastoperatetime" -> false
            "tenantid" -> false
            "t_id" -> false
            else -> true
        }
    }

    /**
     * 将数据库字段类型转成JavaBean的属性类型
     *
     * @param fieldType 数据库字段类型 比如 varchar(64)、bigint、date
     * @return Javapoet的ClassName类
     */
    private fun checkFieldType(fieldType: String): ClassName {
        return when {
            fieldType.lowercase().startsWith("varchar") ||
                    fieldType.lowercase().startsWith("char") ||
                    fieldType.lowercase().startsWith("text") ||
                    fieldType.lowercase().startsWith("enum") ||
                    fieldType.lowercase().startsWith("set") -> ClassName.get("java.lang", "String")

            fieldType.lowercase().startsWith("int") -> ClassName.get("java.lang", "Integer")
            fieldType.lowercase().startsWith("integer") -> ClassName.get("java.lang", "Integer")
            fieldType.lowercase().startsWith("bigint") -> ClassName.get("java.lang", "Long")
            fieldType.lowercase().startsWith("tinyint") -> ClassName.get("java.lang", "Integer")
            fieldType.lowercase().startsWith("smallint") -> ClassName.get("java.lang", "Integer")
            fieldType.lowercase().startsWith("mediumint") -> ClassName.get("java.lang", "Integer")
            fieldType.lowercase().startsWith("float") -> ClassName.get("java.lang", "Float")
            fieldType.lowercase().startsWith("double") -> ClassName.get("java.lang", "Double")
            fieldType.lowercase().startsWith("decimal") ||
                    fieldType.lowercase().startsWith("numeric") -> ClassName.get("java.lang", "BigDecimal")

            fieldType.lowercase().startsWith("date") -> ClassName.get("java.lang", "Date")
            fieldType.lowercase().startsWith("datetime") ||
                    fieldType.lowercase().startsWith("timestamp") -> ClassName.get("java.lang", "Date")

            fieldType.lowercase().startsWith("time") -> ClassName.get("java.lang", "Date")
            fieldType.lowercase().startsWith("year") -> ClassName.get("java.lang", "Date")

            else -> throw IllegalArgumentException("Unsupported field type: $fieldType")
        }
    }

}