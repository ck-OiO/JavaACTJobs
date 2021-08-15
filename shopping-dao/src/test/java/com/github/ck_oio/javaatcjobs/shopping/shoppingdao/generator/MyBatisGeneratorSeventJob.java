package com.github.ck_oio.javaatcjobs.shopping.shoppingdao.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyBatisGeneratorSeventJob {

    public static void main(String[] args) throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException {
        // 记录警告信息
        List<String> warnings = new ArrayList<>();
        // 当生成代码重复时, 覆盖代码
        boolean  overwrite = true;

        Configuration config = null;
        try(InputStream configStream = Files.newInputStream(Path.of("mybatisGenerator.xml"))){
            config = new ConfigurationParser(warnings).parseConfiguration(configStream);
        }
        final DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        final MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        // 执行生成代码
        myBatisGenerator.generate(null);
        for (var warning : warnings) {
            System.out.println(warning);
        }
    }
}
