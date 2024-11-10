package org.easyit.copilot.util

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeSpec
import org.easyit.copilot.domain.`in`.DatabaseGenerateReqIn
import org.easyit.copilot.domain.out.GenRepositoryOut
import javax.lang.model.element.Modifier

object GenRepositoryUtil {

    fun execute(reqIn: DatabaseGenerateReqIn): GenRepositoryOut {
        val className = "${reqIn.entityName}Mapper"
        // 定义BaseMapper<Person>的类型
        val baseMapper: ClassName = ClassName.get("com.baomidou.mybatisplus.core.mapper", "BaseMapper")
        val personClass: ClassName = ClassName.get("com.example", "Person")
        val baseMapperOfPerson: ParameterizedTypeName = ParameterizedTypeName.get(baseMapper, personClass)

        // 创建 PersonMapper 接口，并继承 BaseMapper<Person>
        val mapperClass: TypeSpec = TypeSpec.interfaceBuilder(className)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(baseMapperOfPerson)
            .addJavadoc("这是 MyBatis-Plus 生成的 Mapper 接口，用于操作 Person 表数据。\n")
            .build()

        // 生成 Java 文件
        val javaFile: JavaFile = JavaFile.builder("${reqIn.packageName}.mapper", mapperClass)
            .build()

        // 将生成的代码转换为字符串并打印
        return GenRepositoryOut(
            code = javaFile.toString(),
            fileName = className
        )
    }

}