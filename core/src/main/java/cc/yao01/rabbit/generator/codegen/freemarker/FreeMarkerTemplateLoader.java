package cc.yao01.rabbit.generator.codegen.freemarker;

import cc.yao01.rabbit.generator.config.xmlparser.TemplateParserEntityResolver;
import cc.yao01.rabbit.generator.exception.BizException;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 姚华成
 * @date 2017-10-19
 */
public class FreeMarkerTemplateLoader {
    private SAXReader reader;

    public FreeMarkerTemplateLoader() {
        reader = new SAXReader();
        reader.setValidation(true);
        reader.setEntityResolver(new TemplateParserEntityResolver());

    }

    public FreemarkerRabbitTemplete load(String templatePath) {
        if (!new File(templatePath).exists()) {
            throw new RuntimeException("模板路径指定文件夹不存在：" + templatePath);
        }
        FreemarkerRabbitTemplete rabbitTemplete = new FreemarkerRabbitTemplete();

        rabbitTemplete.setProjectTemplateFiles(loadTemplates(templatePath + "/" + "project"));
        rabbitTemplete.setTableTemplateFiles(loadTemplates(templatePath + "/" + "table"));

        return rabbitTemplete;
    }

    private List<FreemarkerTemplateFile> loadTemplates(String pathName) {
        List<FreemarkerTemplateFile> templateFiles = new ArrayList<>();
        // 需要把模板的全路取到，配置的是相对路径
        File dir = new File(pathName);
        if (!dir.exists()) {
            throw new BizException("模板" + pathName + "不存在！模板文件在类路径的templates目录下");
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".xml")) {
                FreemarkerTemplateFile rabbitTemplateFile = loadTemplateFile(file);
                templateFiles.add(rabbitTemplateFile);
            }
        }
        return templateFiles;
    }

    private FreemarkerTemplateFile loadTemplateFile(File file) {
        Document document;
        try {
            document = reader.read(new FileInputStream(file));
        } catch (Exception e) {
            throw new BizException("加载模板文件" + file.getAbsolutePath() + "时出错：", e);
        }

        FreemarkerTemplateFile templateFile = new FreemarkerTemplateFile();

        Element rootNode = document.getRootElement();
        templateFile.setName(rootNode.attributeValue("name"));
        templateFile.setFileName(rootNode.attributeValue("fileName"));
        templateFile.setFilePath(rootNode.attributeValue("filePath"));
        templateFile.setFileType(rootNode.attributeValue("fileType"));
        templateFile.setContent(rootNode.getText());

        return templateFile;
    }

}
