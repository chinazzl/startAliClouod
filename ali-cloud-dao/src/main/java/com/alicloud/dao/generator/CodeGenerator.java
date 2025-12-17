package com.alicloud.dao.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.nio.file.Paths;
import java.sql.Types;
import java.util.Collections;

/**
 * @author: zhaolin
 * @Date: 2025/12/16
 * @Description:
 **/
public class CodeGenerator {

    public static void main(String[] args) {
        DataSourceConfig.Builder zzl = new DataSourceConfig.Builder("jdbc:mysql://...",
                "xxx", "xxx");

        FastAutoGenerator.create(zzl)
                .globalConfig(builder -> {
                    builder.author("Your Name") // 设置作者
                            .outputDir(Paths.get(System.getProperty("user.dir")) + "\\src\\main\\java")
                            ; // 输出目录
                })
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.SMALLINT) {
                                // 自定义类型转换
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .packageConfig(builder ->
                        builder.parent("com.alicloud.dao.generator") // 设置父包名
                                .moduleName("ali-cloud-dao") // 设置父包模块名
                )
                .strategyConfig(builder ->
                        builder.addInclude("health_profiles,health_objectives,health_metrics,health_assessments,nutrition_goals") // 设置需要生成的表名
                                .addTablePrefix("t_", "c_") // 设置过滤表前缀
                                .entityBuilder()
                                .enableLombok()
                                .addTableFills(new Column("create_at", FieldFill.INSERT))
                                .addTableFills(new Column("update_at", FieldFill.INSERT_UPDATE))
                                .enableTableFieldAnnotation()
                                .idType(IdType.ASSIGN_ID)
                )
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
