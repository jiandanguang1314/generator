package cc.yao01.rabbit.generator;

import cc.yao01.rabbit.generator.codegen.freemarker.FreeMarkerGenerator;
import cc.yao01.rabbit.generator.codegen.freemarker.FreemarkerRabbitTemplete;
import cc.yao01.rabbit.generator.codegen.freemarker.FreemarkerTemplateFile;
import cc.yao01.rabbit.generator.config.RabbitConfig;
import cc.yao01.rabbit.generator.config.dbloader.BizModelLoader;
import cc.yao01.rabbit.generator.config.xmlparser.ConfigFileParser;
import cc.yao01.rabbit.generator.datamodel.ProjectModel;
import cc.yao01.rabbit.generator.datamodel.TableModel;
import cc.yao01.rabbit.generator.exception.BizException;
import cc.yao01.rabbit.generator.output.RabbitGeneratedFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 姚华成
 * @date 2017-10-19
 */
public class RabbitGenerator {

    private static final String WIN_FLAG = "win";
    private RabbitConfig rabbitConfig;
    private FreemarkerRabbitTemplete rabbitTemplete;
    private ProjectModel projectModel;
    private List<TableModel> tableModels;

    RabbitGenerator(File configFile) {
        this.init(configFile);
    }

    public void init(File configFile) {
        ConfigFileParser configFileParser = new ConfigFileParser();
        rabbitConfig = configFileParser.parseConfiguration(configFile);

        projectModel = rabbitConfig.getProjectConfig().genProjectModel();

        rabbitTemplete = rabbitConfig.getProjectConfig().getRabbitTemplete();
        BizModelLoader bizModelLoader = new BizModelLoader();
        tableModels = bizModelLoader.loadBizModel(rabbitConfig.getBizModelsConfig(), projectModel);
        projectModel.setTableModels(tableModels);

    }

    public void generate() {
        // 如果执行配置里要求生成项目目录结构，则创建项目目录结构
        List<RabbitGeneratedFile> projectFiles = generateProject();
        // 如果执行配置里要求生成表相关的结构，则创建表对应的业务文件
        List<RabbitGeneratedFile> tableFiles = generateTables();
        // List<RabbitGeneratedFile> apiFiles = generateApis();

        checkProjectPath();

        writeFiles(projectFiles);
        writeFiles(tableFiles);
        // writeFiles(apiFiles);

        System.out.println("项目根目录地址：" + rabbitConfig.getProjectConfig().getProjectRootPath());
    }

    private void checkProjectPath() {
        String path = projectModel.getProjectRootPath();
        File file = new File(path);
        path = file.getAbsolutePath();
        if (file.exists() && rabbitConfig.getProjectConfig().isOverrideProject()) {
            try {
                Runtime rt = Runtime.getRuntime();
                String os = System.getProperty("os.name");
                if (os.toLowerCase().startsWith(WIN_FLAG)) {
                    rt.exec("cmd.exe /c rd /s/q " + path);
                } else {
                    rt.exec("/bin/sh -c rm -rf " + path);
                }
                Thread.sleep(100);
            } catch (Exception e) {
                throw new RuntimeException("项目根目录文件夹删除失败，请重试！" + path, e);
            }
        }
    }

    private void writeFiles(List<RabbitGeneratedFile> generatedFiles) {
        for (RabbitGeneratedFile rabbitGeneratedFile : generatedFiles) {
            if (rabbitGeneratedFile.isGenetated()) {
                writeFile(rabbitGeneratedFile.getFilePath(), rabbitGeneratedFile.getFileName(),
                        rabbitGeneratedFile.getContent(), 0);
            }
        }
    }

    private void writeFile(String filePath, String fileName, String content, int seq) {
        // 判断文件是否存在，不存在则直接写入文件；
        // 存在则还需要判断是否允许覆盖，如果允许覆盖，则覆盖之；
        // 如果不允许覆盖，则生成新的文件名，并写入文件
        String fullFileName = filePath + File.separatorChar + fileName;
        try {
            File path = new File(filePath);
            if (!path.exists()) {
                if (!path.mkdirs()) {
                    throw new RuntimeException("创建目录失败：" + path.getAbsolutePath());
                }
            }

            File file = new File(seq == 0 ? fullFileName : fullFileName + "." + seq);

            if (!file.exists()) {
                try (FileWriter fw = new FileWriter(file)) {
                    fw.write(content);
                }
            } else {
                writeFile(filePath, fileName, content, seq + 1);
            }
        } catch (IOException e) {
            throw new BizException("写文件：" + fullFileName + "时出错。文件内容为：" + content, e);
        }
    }

    private List<RabbitGeneratedFile> generateProject() {
        List<FreemarkerTemplateFile> templateFiles = rabbitTemplete.getProjectTemplateFiles();
        List<RabbitGeneratedFile> generatedFiles = new ArrayList<>(templateFiles.size());
        if (!rabbitConfig.getProjectConfig().isGenerateProject()) {
            return generatedFiles;
        }

        for (FreemarkerTemplateFile templateFile : templateFiles) {
            FreeMarkerGenerator freeMarkerGenerator = new FreeMarkerGenerator(templateFile, projectModel);
            generatedFiles.add(freeMarkerGenerator.generateFile());
        }
        return generatedFiles;
    }

    private List<RabbitGeneratedFile> generateTables() {
        List<RabbitGeneratedFile> generatedFiles = new ArrayList<>();
        if (!rabbitConfig.getBizModelsConfig().isGenerateTables()) {
            return generatedFiles;
        }
        List<FreemarkerTemplateFile> templateFiles = rabbitTemplete.getTableTemplateFiles();
        List<RabbitGeneratedFile> generatedTableFiles = new ArrayList<>(templateFiles.size());

        for (TableModel tableModel : tableModels) {
            for (FreemarkerTemplateFile templateFile : templateFiles) {
                FreeMarkerGenerator freeMarkerGenerator = new FreeMarkerGenerator(templateFile, tableModel);
                generatedFiles.add(freeMarkerGenerator.generateFile());
            }
            generatedFiles.addAll(generatedTableFiles);
            generatedTableFiles.clear();
        }
        return generatedFiles;
    }

}
