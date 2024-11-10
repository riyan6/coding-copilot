package org.easyit.copilot.util

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import org.easyit.copilot.domain.`in`.DatabaseGenerateReqIn
import org.easyit.copilot.domain.out.GenEntityOut
import org.easyit.copilot.domain.pojo.TableInfo
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

        // 构建类
        val entityClass = TypeSpec.classBuilder(reqIn.entityName)
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(dataAnnotation)
            .addAnnotation(allArgsConstructor)
            .addAnnotation(noArgsConstructor)
            .addAnnotation(builderAnnotation)

        tableInfo.columns.forEach {
            entityClass.addField(
                FieldSpec.builder(String::class.java, it.name)
                    .addModifiers(Modifier.PRIVATE)
                    .build()
            )
        }

        // 生成 Java 文件
        val javaFile = JavaFile.builder("${reqIn.packageName}.pojo.${reqIn.entityName}", entityClass.build())
            .build()

        return GenEntityOut(
            code = javaFile.toString(),
            fileName = "${reqIn.entityName}.java"
        )
    }

}